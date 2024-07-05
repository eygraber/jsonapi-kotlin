package com.eygraber.jsonapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * https://jsonapi.org/format/#error-objects
 */
@Serializable
public data class JsonApiError(
  public val id: String? = null,
  public val links: JsonApiLinks? = null,
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
  ) {
    init {
      if(pointer != null) {
        check(parameter == null && header == null) {
          "Source should only have one of pointer, parameter, and header"
        }
      }

      if(parameter != null) {
        check(pointer == null && header == null) {
          "Source should only have one of pointer, parameter, and header"
        }
      }

      if(header != null) {
        check(pointer == null && parameter == null) {
          "Source should only have one of pointer, parameter, and header"
        }
      }
    }
  }
}
