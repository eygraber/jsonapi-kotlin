package com.eygraber.jsonapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * https://jsonapi.org/format/#document-resource-identifier-objects
 */
@Serializable
public data class JsonApiResourceIdentifier(
  public val id: String? = null,
  public val lid: String? = null,
  public val type: String,
  public val meta: JsonObject? = null,
) {
  public val idOrLid: String = requireNotNull(id ?: lid) {
    "A JsonApiResourceIdentifier must have a non null id or a non null lid"
  }
}
