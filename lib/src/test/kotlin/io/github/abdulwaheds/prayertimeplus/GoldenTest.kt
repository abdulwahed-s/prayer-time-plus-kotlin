package io.github.abdulwaheds.prayertimeplus

import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The verified Appendix-A golden vectors for Sohar, Oman on 2026-06-28 at UTC+4.
 * These encode the astronomy, the parameter table, the adjust pipeline, rounding,
 * timezone and Auto resolution at once — passing them means the port is faithful.
 */
class GoldenTest {
    private val sohar = Coordinates(latitude = 24.3486, longitude = 56.6953, altitude = 5.0)
    private val date = DateComponents(2026, 6, 28)
    private val offset = ZoneOffset.ofHours(4)

    @Test
    fun muslimWorldLeagueMatchesSoharGolden() {
        val times = PrayerTimes(
            coordinates = sohar,
            dateComponents = date,
            calculationParameters = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters(),
            utcOffset = offset,
            countryCode = "OM",
            cityName = "sohar",
        )

        assertEquals("03:59", times.fajr.hhmm(), "Fajr")
        assertEquals("05:27", times.sunrise.hhmm(), "Sunrise")
        assertEquals("12:16", times.dhuhr.hhmm(), "Dhuhr")
        assertEquals("15:37", times.asr.hhmm(), "Asr")
        assertEquals("19:05", times.sunset.hhmm(), "Sunset")
        assertEquals("19:05", times.maghrib.hhmm(), "Maghrib")
        assertEquals("20:28", times.isha.hhmm(), "Isha")
    }

    @Test
    fun omanMatchesSoharGolden() {
        val times = PrayerTimes(
            coordinates = sohar,
            dateComponents = date,
            calculationParameters = CalculationMethod.OMAN.parameters(),
            utcOffset = offset,
            countryCode = "OM",
            cityName = "sohar",
        )

        assertEquals("03:59", times.fajr.hhmm(), "Fajr")
        assertEquals("05:27", times.sunrise.hhmm(), "Sunrise")
        assertEquals("12:21", times.dhuhr.hhmm(), "Dhuhr")
        assertEquals("15:42", times.asr.hhmm(), "Asr")
        assertEquals("19:05", times.sunset.hhmm(), "Sunset")
        assertEquals("19:10", times.maghrib.hhmm(), "Maghrib")
        assertEquals("20:35", times.isha.hhmm(), "Isha")
    }

    @Test
    fun autoResolutionMatchesDocumentedCountries() {
        assertEquals(CalculationMethod.OMAN, AutoMethod.forCountry("OM"))
        assertEquals(CalculationMethod.UMM_AL_QURA, AutoMethod.forCountry("SA"))
        assertEquals(CalculationMethod.EMIRATES, AutoMethod.forCountry("AE"))
        assertEquals(CalculationMethod.TURKEY, AutoMethod.forCountry("TR"))
        assertEquals(CalculationMethod.UOIF, AutoMethod.forCountry("FR"))
        assertEquals(CalculationMethod.NORTH_AMERICA, AutoMethod.forCountry("US"))
        assertEquals(CalculationMethod.MUSLIM_WORLD_LEAGUE, AutoMethod.forCountry("GB"))
        assertEquals(CalculationMethod.KARACHI, AutoMethod.forCountry("PK"))
        assertEquals(CalculationMethod.EGYPTIAN, AutoMethod.forCountry("EG"))
        assertEquals(CalculationMethod.INDONESIA, AutoMethod.forCountry("ID"))
        assertEquals(CalculationMethod.MALAYSIA2, AutoMethod.forCountry("MY"))
    }
}
