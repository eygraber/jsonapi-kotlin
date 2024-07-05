package com.eygraber.jsonapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * https://jsonapi.org/format/#document-jsonapi-object
 */
@Serializable
public data class JsonApiObject(
  public val version: String? = null,
  public val ext: List<String>? = null,
  public val profile: List<String>? = null,
  public val meta: JsonObject? = null,
)
