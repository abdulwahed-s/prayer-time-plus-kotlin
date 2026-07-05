package io.github.abdulwaheds.prayertimeplus

import io.github.abdulwaheds.prayertimeplus.math.arccotDeg
import io.github.abdulwaheds.prayertimeplus.math.fixAngle
import io.github.abdulwaheds.prayertimeplus.math.fixHour
import kotlin.test.Test
import kotlin.test.assertEquals

/** Sanity checks for the degree-math wrappers, including the negative-argument branches. */
class DegreeMathTest {
    @Test
    fun arccotIsTheAtan2Branch() {
        assertEquals(90.0, arccotDeg(0.0), 1e-9)
        assertEquals(45.0, arccotDeg(1.0), 1e-9)
        // arccot(-1) = 135° via atan2, whereas arctan(1/-1) would give -45°.
        assertEquals(135.0, arccotDeg(-1.0), 1e-9)
    }

    @Test
    fun fixAngleWrapsIntoZeroTo360() {
        assertEquals(10.0, fixAngle(370.0), 1e-9)
        assertEquals(350.0, fixAngle(-10.0), 1e-9)
        assertEquals(0.0, fixAngle(360.0), 1e-9)
    }

    @Test
    fun fixHourWrapsIntoZeroTo24() {
        assertEquals(1.0, fixHour(25.0), 1e-9)
        assertEquals(23.0, fixHour(-1.0), 1e-9)
    }
}
