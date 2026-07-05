package io.github.abdulwaheds.prayertimeplus

import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals

/** The Umm al-Qura Ramadan rule adds 30 minutes to Isha, but only in Saudi Arabia. */
class RamadanTest {
    private val mecca = Coordinates(latitude = 21.42667, longitude = 39.82611)
    private val date = DateComponents(2026, 6, 28)
    private val offset = ZoneOffset.ofHours(3)

    @Test
    fun ummAlQuraIshaIsNinetyMinutesAfterMaghrib() {
        val times = ummAlQura(country = "SA", ramadan = false)
        assertEquals(90, minutesBetween(times.maghrib, times.isha))
    }

    @Test
    fun ramadanAddsThirtyMinutesInSaudiArabia() {
        val normal = ummAlQura(country = "SA", ramadan = false)
        val ramadan = ummAlQura(country = "SA", ramadan = true)
        assertEquals(120, minutesBetween(ramadan.maghrib, ramadan.isha))
        assertEquals(30, minutesBetween(normal.isha, ramadan.isha))
    }

    @Test
    fun ramadanBumpIsScopedToSaudiArabia() {
        val outside = ummAlQura(country = "OM", ramadan = true)
        assertEquals(90, minutesBetween(outside.maghrib, outside.isha))
    }

    private fun ummAlQura(country: String, ramadan: Boolean): PrayerTimes = PrayerTimes(
        coordinates = mecca,
        dateComponents = date,
        calculationParameters = CalculationMethod.UMM_AL_QURA.parameters().copy(isRamadan = ramadan),
        utcOffset = offset,
        countryCode = country,
    )

    private fun minutesBetween(from: OffsetDateTime?, to: OffsetDateTime?): Long =
        Duration.between(requireNotNull(from), requireNotNull(to)).toMinutes()
}
