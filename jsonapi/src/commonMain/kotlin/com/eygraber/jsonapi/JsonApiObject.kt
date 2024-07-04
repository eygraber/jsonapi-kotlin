package com.eygraber.jsonapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class JsonApiObject(
  public val version: String? = null,
  public val ext: List<String>? = null,
  public val profile: List<String>? = null,
  public val meta: JsonObject? = null,
)
