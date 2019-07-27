package de.berlin.vivepassion

import de.berlin.vivepassion.GroupByIntervals.GroupByIntervals

case class Config(
                   mode: String = "analyse",
                   alone: Boolean = true,
                   groupBy: GroupByIntervals = GroupByIntervals.Day
    )
