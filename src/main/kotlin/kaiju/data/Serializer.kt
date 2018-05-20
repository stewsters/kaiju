package kaiju.data

import com.google.gson.Gson

interface Serializer<in T, out V> {
    fun serialize(t: T): V
}

infix fun <T : Any?, V> T.serializedAs(s: Serializer<T, V>): V = s.serialize(this)

val JSON = object : Serializer<Any?, String> {
    override fun serialize(t: Any?) = Gson().toJson(t)
}

infix fun <Y> String.deserializeAs(kclass: Class<Y>): Y {
    return Gson().fromJson(this, kclass) as Y
}