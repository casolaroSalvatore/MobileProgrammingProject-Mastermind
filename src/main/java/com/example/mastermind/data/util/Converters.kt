package com.example.mastermind.data.util

import androidx.room.TypeConverter
import com.example.mastermind.data.entity.Move
import com.example.mastermind.domain.model.ColorPeg
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {

    private val gson = Gson()
    private val fmt: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /* ---------- ColorPeg ---------- */
    @TypeConverter
    fun colorPegToString(p: ColorPeg): String = p.name

    @TypeConverter
    fun stringToColorPeg(s: String): ColorPeg = ColorPeg.valueOf(s)

    /* ---------- List<ColorPeg> ---------- */
    @TypeConverter
    fun colorPegListToJson(list: List<ColorPeg>): String = gson.toJson(list)

    @TypeConverter
    fun jsonToColorPegList(json: String): List<ColorPeg> =
        gson.fromJson(json, object : TypeToken<List<ColorPeg>>() {}.type)

    /* ---------- List<Move> ---------- */
    @TypeConverter
    fun moveListToJson(list: List<Move>): String = gson.toJson(list)

    @TypeConverter
    fun jsonToMoveList(json: String): List<Move> =
        gson.fromJson(json, object : TypeToken<List<Move>>() {}.type)

    /* ---------- LocalDateTime ---------- */
    @TypeConverter
    fun localDateTimeToString(t: LocalDateTime): String = fmt.format(t)

    @TypeConverter
    fun stringToLocalDateTime(s: String): LocalDateTime = LocalDateTime.parse(s, fmt)
}



