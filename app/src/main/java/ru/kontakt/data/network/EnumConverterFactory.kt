package ru.kontakt.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class EnumConverterFactory : Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        if (type is Class<*> && type.isEnum) {
            return Converter<Any?, String> { value -> getSerializedNameValue(value as Enum<*>) }
        }
        return super.stringConverter(type, annotations, retrofit)
    }
}

fun <E : Enum<*>> getSerializedNameValue(e: E): String {
    try {
        return e.javaClass.getField(e.name).getAnnotation(SerializedName::class.java).value
    } catch (exception: NoSuchFieldException) {
        exception.printStackTrace()
    }

    return ""
}