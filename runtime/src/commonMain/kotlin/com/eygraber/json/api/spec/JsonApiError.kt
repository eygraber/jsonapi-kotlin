package com.eygraber.json.api.spec

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class JsonApiError(
  public val id: String? = null,
  public val links: Links? = null,
  public val status: String? = null,
  public val code: String? = null,
  public val title: String? = null,
  public val detail: String? = null,
  public val source: Source? = null,
  public val meta: JsonObject? = null,
) {
  @Serializable
  public data class Source(
    public val pointer: String? = null,
    public val parameter: String? = null,
    public val header: String? = null,
  )

  @Serializable
  public data class Links(
    public val about: JsonApiLink? = null,
    public val type: JsonApiLink? = null,
  )
}
