package io.github.abdulwaheds.prayertimeplus

import io.github.abdulwaheds.prayertimeplus.math.julianDay
import io.github.abdulwaheds.prayertimeplus.math.sunPosition
import kotlin.test.Test
import kotlin.test.assertEquals

/** Pure-arithmetic astronomy checks against the documented worked examples. */
class AstronomicalTest {
    @Test
    fun julianDayMatchesWorkedExample() {
        assertEquals(2461219.5, julianDay(2026, 6, 28), 0.0)
    }

    @Test
    fun januaryAndFebruaryBorrowFromThePreviousYear() {
        // 2000-01-01 is the classic Meeus check value.
        assertEquals(2451544.5, julianDay(2000, 1, 1), 0.0)
    }

    @Test
    fun sunPositionMatchesMeccaWorkedExample() {
        // The worked example's intermediates are hand-computed approximations, so
        // this is a coarse sanity check on the formula chain, not an exact fixture.
        val baseDate = julianDay(2026, 6, 28) - 39.82611 / 360.0
        val position = sunPosition(baseDate + 12.0 / 24.0)
        assertEquals(23.262, position.declination, 5e-3)
        assertEquals(-0.054885, position.equationOfTime, 1e-3)
    }

    @Test
    fun sunPositionMatchesSoharIntermediates() {
        val baseDate = julianDay(2026, 6, 28) - 56.6953 / 360.0
        val position = sunPosition(baseDate + 12.0 / 24.0)
        assertEquals(23.2676, position.declination, 1e-3)
        assertEquals(-0.05468, position.equationOfTime, 1e-4)
    }
}
