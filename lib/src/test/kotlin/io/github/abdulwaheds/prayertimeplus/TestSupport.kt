package io.github.abdulwaheds.prayertimeplus

import java.time.OffsetDateTime

/** Formats a time as `"HH:mm"`, or `"--:--"` when undefined, for golden comparisons. */
internal fun OffsetDateTime?.hhmm(): String =
    if (this == null) "--:--" else "%02d:%02d".format(hour, minute)
