package com.example.mastermind.data.util

import androidx.room.TypeConverter
import com.example.mastermind.domain.model.ColorPeg
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/* TypeConverter unificato:
   LocalDateTime <--> String ISO
   List<ColorPeg> <--> JSON
   List<List<ColorPeg>> <--> JSON */

object Converters {

    private val gson = Gson()
    private val fmt: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /* ---------- LocalDateTime ---------- */

    @TypeConverter fun fromDate(t: LocalDateTime?) = t?.format(fmt)
    @TypeConverter fun toDate(s: String?)         = s?.let { LocalDateTime.parse(it, fmt) }

    /* ---------- List<ColorPeg> ---------- */

    @TypeConverter
    fun fromPegList(value: List<ColorPeg>?): String? = value?.let { gson.toJson(it) }

    @TypeConverter
    fun toPegList(value: String?): List<ColorPeg>? =
        value?.let { gson.fromJson(it, Array<ColorPeg>::class.java).toList() }

    /* ---------- List<List<ColorPeg>> ---------- */

    @TypeConverter
    fun fromPegMatrix(value: List<List<ColorPeg>>?): String? = value?.let { gson.toJson(it) }

    @TypeConverter
    fun toPegMatrix(value: String?): List<List<ColorPeg>>? =
        value?.let {
            gson.fromJson(it, Array<Array<ColorPeg>>::class.java)
                .map { row -> row.toList() }
        }
}

