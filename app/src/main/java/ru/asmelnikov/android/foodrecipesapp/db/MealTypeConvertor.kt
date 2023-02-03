package ru.asmelnikov.android.foodrecipesapp.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConvertor {

    @TypeConverter
    fun fromAnyToString(attribute: Any?): String {
        if (attribute == null)
            return ""
        return attribute as String
    }

    @TypeConverter
    fun fromAStringToAny(attribute: String?): Any {
        if (attribute == null)
            return ""
        return attribute
    }
}