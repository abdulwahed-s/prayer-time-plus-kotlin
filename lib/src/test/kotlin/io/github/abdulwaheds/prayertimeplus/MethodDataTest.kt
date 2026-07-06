package io.github.abdulwaheds.prayertimeplus

import io.github.abdulwaheds.prayertimeplus.data.MethodParameters
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Confirms the bundled parameter table is complete. */
class MethodDataTest {
    @Test
    fun everyMethodExceptDubaiHasBundledParameters() {
        for (method in CalculationMethod.entries) {
            if (method == CalculationMethod.DUBAI) continue
            assertTrue(method.key in MethodParameters.keys, "${method.key} missing from the bundled table")
        }
    }

    @Test
    fun dubaiIsResolvedThroughTheFallback() {
        // Dubai deliberately has no row of its own and borrows Muslim World League angles.
        assertFalse(CalculationMethod.DUBAI.key in MethodParameters.keys)
        assertEquals(
            MethodParameters.forKey("mwl").toList(),
            MethodParameters.forKey("dubai").toList(),
        )
    }
}
