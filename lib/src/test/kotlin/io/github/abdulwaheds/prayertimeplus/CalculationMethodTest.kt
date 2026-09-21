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
    fun parametersDefaultToAutomaticHighLatitudeRule() {
        assertEquals(
            HighLatitudeRule.AUTOMATIC,
            CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters().highLatitudeRule,
        )
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

    @Test
    fun autoResolutionUsesCorrectedIraqAndAustriaDefaults() {
        assertEquals(CalculationMethod.IRAQ, AutoMethod.forCountry("IQ"))
        assertEquals(CalculationMethod.AUSTRIA, AutoMethod.forCountry("AT"))
    }

    @Test
    fun autoResolutionCoversEveryDedicatedNationalMethod() {
        // Every supported country-wide preset. Global/regional defaults and city-only
        // variants are intentionally excluded.
        val expected =
            mapOf(
                "AE" to CalculationMethod.EMIRATES,
                "AT" to CalculationMethod.AUSTRIA,
                "BE" to CalculationMethod.BELGIUM,
                "CH" to CalculationMethod.SWITZERLAND,
                "CZ" to CalculationMethod.CZECH,
                "DZ" to CalculationMethod.ALGERIA,
                "EG" to CalculationMethod.EGYPTIAN,
                "FR" to CalculationMethod.UOIF,
                "ID" to CalculationMethod.INDONESIA,
                "IQ" to CalculationMethod.IRAQ,
                "JO" to CalculationMethod.JORDAN,
                "KR" to CalculationMethod.SOUTH_KOREA,
                "KW" to CalculationMethod.KUWAIT,
                "KZ" to CalculationMethod.KAZAKHSTAN,
                "LU" to CalculationMethod.LUXEMBOURG,
                "LY" to CalculationMethod.LIBYA,
                "MA" to CalculationMethod.MOROCCO,
                "MV" to CalculationMethod.MALDIVES,
                "MY" to CalculationMethod.MALAYSIA2,
                "OM" to CalculationMethod.OMAN,
                "PK" to CalculationMethod.KARACHI,
                "PS" to CalculationMethod.PALESTINE,
                "QA" to CalculationMethod.QATAR,
                "SA" to CalculationMethod.UMM_AL_QURA,
                "SD" to CalculationMethod.SUDAN,
                "SY" to CalculationMethod.SYRIA,
                "TJ" to CalculationMethod.TAJIKISTAN,
                "TN" to CalculationMethod.TUNISIA,
                "TR" to CalculationMethod.TURKEY,
            )

        for ((countryCode, method) in expected) {
            assertEquals(method, AutoMethod.forCountry(countryCode), countryCode)
        }
    }
}
