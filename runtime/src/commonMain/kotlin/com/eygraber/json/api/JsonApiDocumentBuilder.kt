package com.eygraber.json.api

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

public object JsonApiDocumentBuilder {
  public data class Id(
    val id: String?,
    val lid: String?,
  ) {
    public val idOrLid: String get() = requireNotNull(id ?: lid) {
      "A JsonApiResource must have a non null id or a non null lid"
    }
  }

  public fun JsonObject.id(): Id {
    val id = this["id"]?.jsonPrimitive?.contentOrNull
    val lid = this["lid"]?.jsonPrimitive?.contentOrNull

    requireNotNull(id ?: lid) {
      "A JsonTypeApi must have a non null id or a non null lid"
    }

    return Id(
      id = id,
      lid = lid,
    )
  }

  public fun JsonObject.relationshipId(key: String): Id {
    val obj = this[key]?.jsonObject
    val id = obj?.get("id")?.jsonPrimitive?.contentOrNull
    val lid = obj?.get("lid")?.jsonPrimitive?.contentOrNull

    requireNotNull(id ?: lid) {
      "A JsonTypeApi must have a non null id or a non null lid for a relationship"
    }

    return Id(
      id = id,
      lid = lid,
    )
  }

  public fun JsonObject.filterIdAndRelationships(
    relationshipPropertyNames: Set<String>,
  ): JsonObject = buildJsonObject {
    filterNot {
      it.key == "id" || it.key == "lid" || it.key in relationshipPropertyNames
    }.forEach { (k, v) ->
      put(k, v)
    }
  }
}
