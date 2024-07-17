package com.eygraber.jsonapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * https://jsonapi.org/format/#document-resource-identifier-objects
 */
@Serializable
public data class JsonApiResourceIdentifier(
  public val type: String,
  public val id: JsonApiId,
  public val lid: JsonApiId = JsonApiId.NoId,
  public val meta: JsonObject? = null,
) {
  init {
    requireNotNull(id.isSpecified || lid.isSpecified) {
      "A JsonApiResourceIdentifier must have a non null id or a non null lid"
    }
  }
}
