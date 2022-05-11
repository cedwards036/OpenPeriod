package dev.christopheredwards.openperiod.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

enum class FlowLevel {
    LIGHT, MEDIUM, HEAVY
}

@Entity(tableName = "p_date", indices = [Index(value = ["date"], unique = true)])
data class PDate(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "text_note") val textNote: String = "",
    @ColumnInfo(name = "was_intimate") val wasIntimate: Boolean = false,
    @ColumnInfo(name = "used_protection") val usedProtection: Boolean? = null,
    @ColumnInfo(name = "period_started") var periodStarted: Boolean = false,
    @ColumnInfo(name = "period_ended") val periodEnded: Boolean = false,
    @ColumnInfo(name = "had_spotting") val hadSpotting: Boolean = false,
    @ColumnInfo(name = "flow_level") val flowLevel: FlowLevel? = null
)