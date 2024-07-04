package com.eygraber.json.api.spec

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class JsonApiResourceIdentifier(
  public val id: String? = null,
  public val lid: String? = null,
  public val type: String,
  public val meta: JsonObject? = null,
) {
  public val idOrLid: String = requireNotNull(id ?: lid) {
    "A JsonApiResource must have a non null id or a non null lid"
  }
}
