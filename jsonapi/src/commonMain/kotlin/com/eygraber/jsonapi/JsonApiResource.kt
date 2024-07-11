package com.eygraber.jsonapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * https://jsonapi.org/format/#document-resource-objects
 */
@Serializable
public data class JsonApiResource(
  public val id: String? = null,
  public val lid: String? = null,
  public val type: String,
  public val attributes: JsonObject? = null,
  public val relationships: Map<String, JsonApiRelationship>? = null,
  public val links: JsonApiLinks? = null,
  public val meta: JsonObject? = null,
) {
  public fun requireIdOrLid(): String = requireNotNull(id ?: lid) {
    "A JsonApiResource must have a non null id or a non null lid"
  }
}

public fun List<JsonApiResource>.find(id: String, type: String): JsonApiResource? = find { resource ->
  (resource.id == id || resource.lid == id) && resource.type == type
}

public fun List<JsonApiResource>.withType(type: String): List<JsonApiResource> = filter { resource ->
  resource.type == type
}
