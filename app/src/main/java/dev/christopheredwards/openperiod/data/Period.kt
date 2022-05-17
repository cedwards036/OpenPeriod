package dev.christopheredwards.openperiod.data

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.time.LocalDate

@DatabaseView(
    """
    SELECT period_start.date AS start_date
         , period_end.date AS end_date
         , next_period_start.date - 1 AS cycle_end_date
         , period_end.date - period_start.date + 1 AS period_length
         , next_period_start.date - period_start.date AS cycle_length
    FROM p_date AS period_start
    -- a period spans from a start date to the next end date (if any)
    LEFT JOIN p_date AS period_end
        ON period_end.date = (
            SELECT MIN(p_date.date)
            FROM p_date
            WHERE p_date.date >= period_start.date  -- a period can end on the day it started
                AND p_date.period_ended
        )    
    -- a cycle spans from a start date to the next start date (if any)
    LEFT JOIN p_date AS next_period_start
        ON next_period_start.date = (
            SELECT MIN(p_date.date)
            FROM p_date
            WHERE p_date.date > period_start.date
                AND p_date.period_started
        )
    WHERE period_start.period_started
    """
)
data class Period(
    @ColumnInfo(name = "start_date") val startDate: LocalDate? = null,
    @ColumnInfo(name = "end_date") val endDate: LocalDate? = null,
    @ColumnInfo(name = "cycle_end_date") val cycleEndDate: LocalDate? = null,
    @ColumnInfo(name = "period_length") val periodLength: Int? = null,
    @ColumnInfo(name = "cycle_length") val cycleLength: Int? = null
)