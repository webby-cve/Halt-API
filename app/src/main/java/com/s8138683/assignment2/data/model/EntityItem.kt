package com.s8138683.assignment2.data.model

import android.os.Parcelable
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.parcelize.Parcelize

/** A single key/value property of an entity, e.g. "habitat" = "Forest". */
@Parcelize
data class FieldEntry(val name: String, val value: String) : Parcelable

/**
 * Domain representation of one dashboard entity.
 * All properties are kept except "description", which is stored separately
 * so it can be shown on the Details screen.
 */
@Parcelize
data class EntityItem(
    val fields: List<FieldEntry>,
    val description: String,
) : Parcelable

/** Result of a successful dashboard call. */
data class DashboardData(
    val entities: List<EntityItem>,
    val entityTotal: Int,
)

/** Maps a raw JSON entity from the API into a display-friendly [EntityItem]. */
internal fun JsonObject.toEntityItem(): EntityItem {
    val description = this["description"]
        ?.takeUnless { it.isJsonNull }
        ?.toDisplayString()
        ?: "No description available."

    val fields = entrySet()
        .filter { it.key != "description" && !it.value.isJsonNull }
        .map { FieldEntry(it.key, it.value.toDisplayString()) }

    return EntityItem(fields = fields, description = description)
}

private fun JsonElement.toDisplayString(): String {
    if (!isJsonPrimitive) return toString()
    val primitive = asJsonPrimitive
    return if (primitive.isString) primitive.asString else primitive.toString()
}
