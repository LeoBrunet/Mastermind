package com.example.lhmind.data.local.converter

import androidx.room.TypeConverter
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.domain.model.Peg
import com.example.lhmind.domain.model.PegColor
import java.io.File.separator
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromGameStatus(value: GameStatus): String {
        return value.name
    }

    @TypeConverter
    fun toGameStatus(value: String): GameStatus {
        return GameStatus.valueOf(value)
    }

    @TypeConverter
    fun fromListOfPegs(pegs: List<Peg>): String {
        return pegs.joinToString(",") { "${it.position},${it.color.name}" }
    }

    @TypeConverter
    fun toListOfPegs(value: String): List<Peg> {
        if (value.isEmpty()) return emptyList()

        return value.split(",").chunked(2).map { pegData ->
            Peg(
                position = pegData[0].toInt(),
                color = PegColor.valueOf(pegData[1])
            )
        }
    }

    @TypeConverter
    fun fromPegColor(value: PegColor): String {
        return value.name
    }

    @TypeConverter
    fun toPegColor(value: String): PegColor {
        return PegColor.valueOf(value)
    }
}