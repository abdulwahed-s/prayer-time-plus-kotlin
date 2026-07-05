package io.github.abdulwaheds.prayertimeplus

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Preset lookups and the Dubai fallback. */
class CalculationMethodTest {
    @Test
    fun fromKeyRoundTripsEveryMethod() {
        for (method in CalculationMethod.entries) {
            assertEquals(method, CalculationMethod.fromKey(method.key))
        }
    }

    @Test
    fun parametersCarryTheMethodKey() {
        assertEquals("makkah", CalculationMethod.UMM_AL_QURA.parameters().method)
        assertEquals("egypt", CalculationMethod.EGYPTIAN.parameters().method)
    }

    @Test
    fun ummAlQuraUsesAnIntervalIsha() {
        val params = CalculationMethod.UMM_AL_QURA.parameters()
        assertTrue(params.ishaIsInterval)
        assertEquals(90.0, params.ishaValue)
        assertEquals(18.5, params.fajrAngle)
    }

    @Test
    fun dubaiFallsBackToMuslimWorldLeagueAngles() {
        val dubai = CalculationMethod.DUBAI.parameters()
        val mwl = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters()
        assertEquals(mwl.fajrAngle, dubai.fajrAngle)
        assertEquals(mwl.ishaValue, dubai.ishaValue)
        assertEquals("dubai", dubai.method)
    }

    @Test
    fun autoResolutionIsCaseInsensitiveAndDefaultsToMwl() {
        assertEquals(CalculationMethod.OMAN, AutoMethod.forCountry("om"))
        assertEquals(CalculationMethod.MUSLIM_WORLD_LEAGUE, AutoMethod.forCountry("ZZ"))
    }
}
