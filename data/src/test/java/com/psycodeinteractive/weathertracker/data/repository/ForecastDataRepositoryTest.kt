import androidx.datastore.core.DataStore
import com.psycodeinteractive.weathertracker.data.model.ConditionDataModel
import com.psycodeinteractive.weathertracker.data.model.ForecastDataModel
import com.psycodeinteractive.weathertracker.data.model.LocationDataModel
import com.psycodeinteractive.weathertracker.data.model.QueriedCityDataModel
import com.psycodeinteractive.weathertracker.data.model.WeatherDataModel
import com.psycodeinteractive.weathertracker.data.repository.ForecastDataRepository
import com.psycodeinteractive.weathertracker.data.source.local.model.ForecastLocalModel
import com.psycodeinteractive.weathertracker.data.source.remote.ForecastApiService
import com.psycodeinteractive.weathertracker.domain.model.ConditionDomainModel
import com.psycodeinteractive.weathertracker.domain.model.ForecastDomainModel
import com.psycodeinteractive.weathertracker.domain.model.LocationDomainModel
import com.psycodeinteractive.weathertracker.domain.model.WeatherDomainModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before

class ForecastDataRepositoryTest {

    private lateinit var forecastApiServiceFake: FakeForecastApiService
    private lateinit var dataStoreFake: FakeDataStore
    private lateinit var repository: ForecastDataRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        forecastApiServiceFake = FakeForecastApiService()
        dataStoreFake = FakeDataStore()
        repository = ForecastDataRepository(forecastApiServiceFake, dataStoreFake, testDispatcher)
    }

    @Test
    fun `searchCities shows available cities matching query`() = runTest {
        // Given a city exists in the system
        val queriedCity = QueriedCityDataModel(
            id = 1,
            name = "CityA",
            region = "RegionA",
            country = "CountryA",
            lat = 10.0,
            lon = 20.0,
            url = "http://citya.com"
        )
        forecastApiServiceFake.addQueriedCity(queriedCity)
        forecastApiServiceFake.addForecast(
            ForecastDataModel(
                LocationDataModel("CityA"),
                WeatherDataModel(20.0, ConditionDataModel("Sunny"), 60, 10.0, 2.0)
            )
        )

        // When the user searches for a city
        val result = repository.searchCities("CityA").getOrThrow()

        // Then the system returns matching cities
        assertEquals(1, result.size)
        assertEquals("CityA", result[0].location.name)
    }

    @Test
    fun `getSavedForecast returns no forecast when none is saved`() = runTest {
        // Given no forecast is saved
        dataStoreFake.updateData { ForecastLocalModel.Empty }

        // When the user tries to retrieve the saved forecast
        val result = repository.getSavedForecast()

        // Then the system shows no forecast
        assertEquals(null, result)
    }

    @Test
    fun `getSavedForecast returns the saved forecast`() = runTest {
        // Given a forecast is saved
        val savedForecast = ForecastLocalModel.Forecast(
            ForecastDataModel(
                LocationDataModel("CityB"),
                WeatherDataModel(15.0, ConditionDataModel("Rainy"), 70, 14.0, 3.0)
            )
        )
        dataStoreFake.updateData { savedForecast }

        // When the user retrieves the saved forecast
        val result = repository.getSavedForecast()

        // Then the system shows the saved forecast
        assertEquals("CityB", result?.location?.name)
        assertEquals(15.0, result?.weather?.temperatureCelsius)
        assertEquals("https:Rainy", result?.weather?.condition?.icon)
    }

    @Test
    fun `refreshSavedForecast shows an error when no forecast is saved`() = runTest {
        // Given no forecast is saved
        dataStoreFake.updateData { ForecastLocalModel.Empty }

        // When the user tries to refresh the saved forecast
        val exception = assertFailsWith<IllegalArgumentException> {
            repository.refreshSavedForecast().getOrThrow()
        }

        // Then the system shows an error message
        assertEquals("No saved forecast found", exception.message)
    }

    @Test
    fun `saveForecast stores a new forecast`() = runTest {
        // Given a new forecast is provided
        val forecast = ForecastDomainModel(
            location = LocationDomainModel("CityC"),
            weather = WeatherDomainModel(
                temperatureCelsius = 25.0,
                condition = ConditionDomainModel("Cloudy"),
                humidity = 65,
                uv = 3.5,
                feelsLikeCelsius = 24.0
            ),
            isSaved = false
        )

        // When the user saves the forecast
        repository.saveForecast(forecast)

        // Then the system updates the saved forecast
        val savedForecast = dataStoreFake.data.first() as ForecastLocalModel.Forecast
        assertEquals("CityC", savedForecast.forecast.location.name)
        assertEquals(25.0, savedForecast.forecast.weather.tempC)
        assertEquals("Cloudy", savedForecast.forecast.weather.condition.icon)
    }

    // Fakes for dependencies
    class FakeForecastApiService : ForecastApiService {
        private val queriedCities = mutableListOf<QueriedCityDataModel>()
        private val forecasts = mutableMapOf<String, ForecastDataModel>()

        fun addQueriedCity(city: QueriedCityDataModel) {
            queriedCities.add(city)
        }

        fun addForecast(forecast: ForecastDataModel) {
            forecasts[forecast.location.name] = forecast
        }

        override suspend fun search(query: String): List<QueriedCityDataModel> {
            return queriedCities.filter { it.name.contains(query, ignoreCase = true) }
        }

        override suspend fun fetchForecast(query: String): ForecastDataModel {
            return forecasts[query] ?: throw IllegalArgumentException("City not found")
        }
    }

    class FakeDataStore : DataStore<ForecastLocalModel> {

        private var _data: ForecastLocalModel = ForecastLocalModel.Empty

        override val data: Flow<ForecastLocalModel>
            get() = flow {
                emit(_data)
            }

        override suspend fun updateData(transform: suspend (t: ForecastLocalModel) -> ForecastLocalModel): ForecastLocalModel  {
            _data = transform(_data)
            return _data
        }
    }
}
