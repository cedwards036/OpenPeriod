package dev.christopheredwards.openperiod.data

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.time.LocalDate

@DatabaseView(
    """
    SELECT period_start.date AS start_date
         , NULL AS predicted_start_date
         , period_end.date AS end_date
         , NULL AS predicted_end_date
         , next_period_start.date - 1 AS cycle_end_date
         , NULL AS predicted_cycle_end_date
         , period_end.date - period_start.date + 1 AS period_length
         , next_period_start.date - period_start.date AS cycle_length
    FROM p_date AS period_start
    LEFT JOIN p_date AS period_end
        ON period_end.date = (
            SELECT MIN(p_date.date)
            FROM p_date
            /*
            Use HAVING instead of WHERE to ensure sub-query returns nothing (rather than 1 "blank"
            row) if no dates match the criteria
            */
            GROUP BY p_date.date
            HAVING p_date.date >= period_start.date
                AND p_date.period_ended
        )    
    LEFT JOIN p_date AS next_period_start
        ON next_period_start.date = (
            SELECT MIN(p_date.date)
            FROM p_date
            GROUP BY p_date.date 
            HAVING p_date.date >= period_end.date  -- see above comment about HAVING
                AND p_date.period_started
        )
    WHERE period_start.period_started
    """
)
data class Period(
    @ColumnInfo(name = "start_date") val startDate: LocalDate? = null,
    @ColumnInfo(name = "predicted_start_date") val predictedStartDate: LocalDate? = null,
    @ColumnInfo(name = "end_date") val endDate: LocalDate? = null,
    @ColumnInfo(name = "predicted_end_date") val predictedEndDate: LocalDate? = null,
    @ColumnInfo(name = "cycle_end_date") val cycleEndDate: LocalDate? = null,
    @ColumnInfo(name = "predicted_cycle_end_date") val predictedCycleEndDate: LocalDate? = null,
    @ColumnInfo(name = "period_length") val periodLength: Int? = null,
    @ColumnInfo(name = "cycle_length") val cycleLength: Int? = null
    //TODO add "isPredictedPeriod" flag that is a derived property?
)