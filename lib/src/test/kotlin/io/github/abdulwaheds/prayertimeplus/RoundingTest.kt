package io.github.abdulwaheds.prayertimeplus

import io.github.abdulwaheds.prayertimeplus.engine.roundedMinuteOfDay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Verifies nearest-minute rounding: add 30 seconds, then floor to the minute. */
class RoundingTest {
    @Test
    fun secondsBelowThirtyRoundDown() {
        // 12:25:24
        assertEquals(12 * 60 + 25, roundedMinuteOfDay(12.0 + 25.4 / 60.0))
    }

    @Test
    fun secondsAboveThirtyRoundUp() {
        // 12:25:36
        assertEquals(12 * 60 + 26, roundedMinuteOfDay(12.0 + 25.6 / 60.0))
    }

    @Test
    fun undefinedTimeBecomesNull() {
        assertNull(roundedMinuteOfDay(Double.NaN))
    }

    @Test
    fun roundingPastMidnightWrapsToZero() {
        // 23:59:40 rolls forward to 00:00 rather than 24:00.
        assertEquals(0, roundedMinuteOfDay(23.0 + 59.0 / 60.0 + 40.0 / 3600.0))
    }
}
