package io.github.abdulwaheds.prayertimeplus.example

import io.github.abdulwaheds.prayertimeplus.AutoMethod
import io.github.abdulwaheds.prayertimeplus.Coordinates
import io.github.abdulwaheds.prayertimeplus.PrayerTimes
import io.github.abdulwaheds.prayertimeplus.SunnahTimes
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val CLOCK = DateTimeFormatter.ofPattern("HH:mm")

private fun clock(time: OffsetDateTime?): String = time?.format(CLOCK) ?: "--:--"

/** Prints today's prayer times for a sample location using the Auto-resolved method. */
fun main() {
    val cityName = "Sohar"
    val countryCode = "OM"
    val coordinates = Coordinates(latitude = 24.3486, longitude = 56.6953, altitude = 5.0)
    val utcOffset = ZoneOffset.ofHours(4)

    val method = AutoMethod.forCountry(countryCode)
    val times =
        PrayerTimes.today(
            coordinates = coordinates,
            parameters = method.parameters(),
            utcOffset = utcOffset,
            countryCode = countryCode,
            cityName = cityName,
        )
    val date = times.dateComponents

    println("prayer-time-plus")
    println("$cityName, $countryCode | %04d-%02d-%02d | method ${method.name}".format(date.year, date.month, date.day))
    println("  Fajr     ${clock(times.fajr)}")
    println("  Sunrise  ${clock(times.sunrise)}")
    println("  Dhuhr    ${clock(times.dhuhr)}")
    println("  Asr      ${clock(times.asr)}")
    println("  Maghrib  ${clock(times.maghrib)}")
    println("  Isha     ${clock(times.isha)}")

    val now = OffsetDateTime.now(utcOffset)
    println("Current prayer: ${times.currentPrayer(now)}")
    println("Next prayer:    ${times.nextPrayer(now)}")

    val sunnah = SunnahTimes(times)
    println("Middle of the night:     ${clock(sunnah.middleOfTheNight)}")
    println("Last third of the night: ${clock(sunnah.lastThirdOfTheNight)}")
}
