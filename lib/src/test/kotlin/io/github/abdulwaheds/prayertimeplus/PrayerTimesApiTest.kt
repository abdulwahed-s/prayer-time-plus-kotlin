package io.github.abdulwaheds.prayertimeplus

import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Current/next prayer selection, [PrayerTimes.timeForPrayer], and Sunnah times. */
class PrayerTimesApiTest {
    private val offset = ZoneOffset.ofHours(4)
    private val times =
        PrayerTimes(
            coordinates = Coordinates(24.3486, 56.6953, 5.0),
            dateComponents = DateComponents(2026, 6, 28),
            calculationParameters = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters(),
            utcOffset = offset,
            countryCode = "OM",
        )

    private fun at(
        hour: Int,
        minute: Int,
    ): OffsetDateTime = OffsetDateTime.of(2026, 6, 28, hour, minute, 0, 0, offset)

    @Test
    fun beforeFajrThereIsNoCurrentPrayer() {
        assertEquals(Prayer.NONE, times.currentPrayer(at(2, 0)))
        assertEquals(Prayer.FAJR, times.nextPrayer(at(2, 0)))
    }

    @Test
    fun middayFallsInTheDhuhrWindow() {
        assertEquals(Prayer.DHUHR, times.currentPrayer(at(13, 0)))
        assertEquals(Prayer.ASR, times.nextPrayer(at(13, 0)))
    }

    @Test
    fun afterIshaThereIsNoNextPrayer() {
        assertEquals(Prayer.ISHA, times.currentPrayer(at(21, 0)))
        assertEquals(Prayer.NONE, times.nextPrayer(at(21, 0)))
    }

    @Test
    fun timeForPrayerMatchesTheProperties() {
        assertEquals(times.fajr, times.timeForPrayer(Prayer.FAJR))
        assertEquals(times.isha, times.timeForPrayer(Prayer.ISHA))
        assertEquals(null, times.timeForPrayer(Prayer.NONE))
    }

    @Test
    fun sunnahTimesFallBetweenMaghribAndTheNextFajr() {
        val sunnah = SunnahTimes(times)
        val middle = assertNotNull(sunnah.middleOfTheNight)
        val lastThird = assertNotNull(sunnah.lastThirdOfTheNight)
        val maghrib = assertNotNull(times.maghrib)
        assertTrue(middle.isAfter(maghrib), "middle of the night is after Maghrib")
        assertTrue(lastThird.isAfter(middle), "last third is after the middle")
    }
}
