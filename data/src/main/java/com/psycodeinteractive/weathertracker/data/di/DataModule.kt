package com.psycodeinteractive.weathertracker.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.psycodeinteractive.weathertracker.data.BuildConfig
import com.psycodeinteractive.weathertracker.data.NetworkConstants.BASE_URL
import com.psycodeinteractive.weathertracker.data.network.interceptor.ErrorInterceptor
import com.psycodeinteractive.weathertracker.data.repository.ForecastDataRepository
import com.psycodeinteractive.weathertracker.data.source.local.DataStoreSerializer
import com.psycodeinteractive.weathertracker.data.source.local.model.ForecastLocalModel
import com.psycodeinteractive.weathertracker.data.source.remote.ForecastApiService
import com.psycodeinteractive.weathertracker.domain.repository.ForecastRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val apiKeyFieldName = "key"

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    fun providesForecastRepository(
        forecastApiService: ForecastApiService,
        dataStore: DataStore<ForecastLocalModel>,
        ioDispatcher: CoroutineDispatcher
    ): ForecastRepository = ForecastDataRepository(
        forecastApiService = forecastApiService,
        dataStore = dataStore,
        ioDispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun providesOkhttpClient(
        @ApplicationContext context: Context,
        json: Json
    ): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val url = chain.request().url
                .newBuilder()
                .addQueryParameter(apiKeyFieldName, BuildConfig.WEATHER_API_KEY)
                .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }
        .addInterceptor(
            ChuckerInterceptor.Builder(context)
                .collector(
                    ChuckerCollector(
                        context = context,
                        showNotification = true,
                        retentionPeriod = RetentionManager.Period.ONE_WEEK,
                    )
                )
                .maxContentLength(Long.MAX_VALUE)
                .alwaysReadResponseBody(true)
                .build()
        )
        .addInterceptor(ErrorInterceptor(json))
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    @Provides
    @Singleton
    fun providesForecastApiService(
        retrofit: Retrofit
    ): ForecastApiService = retrofit.create(ForecastApiService::class.java)

    @Provides
    @Singleton
    fun providesJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun providesForecastDataStore(
        @ApplicationContext applicationContext: Context,
        scope: CoroutineScope,
        ioDispatcher: CoroutineDispatcher,
        json: Json
    ): DataStore<ForecastLocalModel> {
        return DataStoreFactory.create(
            produceFile = { applicationContext.dataStoreFile("nEVWe89QMF01") },
            serializer = DataStoreSerializer(
                json,
                ForecastLocalModel.serializer(),
                ForecastLocalModel.Empty
            ),
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            corruptionHandler = ReplaceFileCorruptionHandler { ForecastLocalModel.Empty }
        )
    }
}
