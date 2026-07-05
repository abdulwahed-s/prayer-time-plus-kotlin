package io.github.abdulwaheds.prayertimeplus

import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * High-latitude behaviour for Oslo (59.9°N) at the June solstice, where the sun
 * never reaches the 18° depression, so the angle-based Fajr and Isha are undefined.
 */
class HighLatitudeTest {
    private val oslo = Coordinates(latitude = 59.9139, longitude = 10.7522)
    private val date = DateComponents(2026, 6, 21)
    private val offset = ZoneOffset.ofHours(2)
    private val base = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters()

    @Test
    fun noneLeavesFajrAndIshaUndefined() {
        val times = timesWith(HighLatitudeRule.NONE)
        assertNull(times.fajr, "Fajr should be undefined under NONE")
        assertNull(times.isha, "Isha should be undefined under NONE")
        // Sunrise/Sunset still resolve — the sun does cross the horizon.
        assertNotNull(times.sunrise)
        assertNotNull(times.sunset)
    }

    @Test
    fun seventhOfTheNightDefinesFajrAndIsha() {
        val times = timesWith(HighLatitudeRule.SEVENTH_OF_THE_NIGHT)
        assertNotNull(times.fajr)
        assertNotNull(times.isha)
    }

    @Test
    fun differentRulesProduceDifferentTimes() {
        val seventh = timesWith(HighLatitudeRule.SEVENTH_OF_THE_NIGHT)
        val middle = timesWith(HighLatitudeRule.MIDDLE_OF_THE_NIGHT)
        assertNotEquals(seventh.isha, middle.isha)
        assertNotEquals(seventh.fajr, middle.fajr)
    }

    @Test
    fun autoModeFallsBackWhenAngleTimesAreDegenerate() {
        val auto = PrayerTimes(oslo, date, base.copy(highLatitudeRule = null), offset)
        assertNotNull(auto.fajr, "auto mode should fall back to a night fraction")
        assertNotNull(auto.isha, "auto mode should fall back to a night fraction")
    }

    private fun timesWith(rule: HighLatitudeRule): PrayerTimes =
        PrayerTimes(oslo, date, base.copy(highLatitudeRule = rule), offset)
}
