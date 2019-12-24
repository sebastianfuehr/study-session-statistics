package de.berlin.vivepassion

import de.berlin.vivepassion.GroupByIntervals.GroupByIntervals

/**
 * Case class which is needed by the OptionParser class of the scopt package.
 *
 * @param debug
 * @param mode
 * @param alone
 * @param groupBy
 * @param form
 * @param course
 * @param date
 * @param startTime
 * @param endTime
 * @param pause
 * @param comment
 * @param semester
 *
 * @see scopt.OptionParser
 *
 * @author Sebastian Führ
 * @version 0.1
 */
case class Config(
                   debug: Boolean = false,
                   mode: String = "analyse",
                   alone: Boolean = true,
                   groupBy: GroupByIntervals = GroupByIntervals.Day,
                   // study session attributes
                   form: String = "",
                   course: String = "",
                   date: String = "",
                   startTime: String = "",
                   endTime: String = "",
                   pause: Int = 0,
                   comment: String = "",
                   semester: String = VPSConfiguration.properties.getProperty("current_semester")
                 )
