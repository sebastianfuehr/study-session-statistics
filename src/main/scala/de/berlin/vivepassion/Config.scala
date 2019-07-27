package de.berlin.vivepassion

import java.time.LocalDateTime

import de.berlin.vivepassion.GroupByIntervals.GroupByIntervals

case class Config(
                   debug: Boolean = false,
                   mode: String = "analyse",
                   alone: Boolean = true,
                   groupBy: GroupByIntervals = GroupByIntervals.Day,
                   // study session attributes
                   form: String = "",
                   course: String = "",
                   startTime: LocalDateTime = null,
                   endTime: LocalDateTime = null,
                   pause: Int = 0,
                   comment: String = ""
                 )
