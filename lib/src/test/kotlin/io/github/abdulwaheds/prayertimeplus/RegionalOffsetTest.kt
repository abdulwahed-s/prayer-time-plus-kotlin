package io.github.abdulwaheds.prayertimeplus

import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Every method equals a same-angle baseline with its per-prayer offsets removed,
 * shifted by exactly those offsets. Because the offsets are whole minutes applied
 * before the nearest-minute rounding, the shift commutes with rounding and is exact.
 *
 * Interval Isha inherits the Maghrib offset (it is measured from Maghrib), so its
 * expected shift is the Maghrib offset plus the Isha offset.
 */
class RegionalOffsetTest {
    private val coordinates = Coordinates(latitude = 24.3486, longitude = 56.6953)
    private val date = DateComponents(2026, 6, 28)
    private val offset = ZoneOffset.ofHours(4)

    @Test
    fun everyMethodIsItsBaselineShiftedByItsOffsets() {
        for (method in CalculationMethod.entries) {
            val params = method.parameters()
            val baseline = params.copy(methodAdjustments = PrayerAdjustments())
            val full = timesFor(params)
            val base = timesFor(baseline)
            val adjustments = params.methodAdjustments
            val ishaShift =
                if (params.ishaIsInterval) adjustments.maghrib + adjustments.isha else adjustments.isha

            assertShifted(base.fajr, full.fajr, adjustments.fajr, "$method Fajr")
            assertShifted(base.sunrise, full.sunrise, adjustments.sunrise, "$method Sunrise")
            assertShifted(base.dhuhr, full.dhuhr, adjustments.dhuhr, "$method Dhuhr")
            assertShifted(base.asr, full.asr, adjustments.asr, "$method Asr")
            assertShifted(base.maghrib, full.maghrib, adjustments.maghrib, "$method Maghrib")
            assertShifted(base.isha, full.isha, ishaShift, "$method Isha")
        }
    }

    private fun timesFor(params: CalculationParameters): PrayerTimes =
        PrayerTimes(coordinates, date, params, offset)

    private fun assertShifted(base: OffsetDateTime?, full: OffsetDateTime?, minutes: Int, label: String) {
        assertNotNull(base, "$label baseline should be defined")
        assertNotNull(full, "$label method should be defined")
        assertEquals(base.plusMinutes(minutes.toLong()), full, label)
    }
}
