package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiError
import com.eygraber.jsonapi.JsonApiLinks
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject

public class JsonApiErrorBuilder(
  public var id: String? = null,
  @PublishedApi internal var errorLinks: JsonApiLinks? = null,
  public var status: String? = null,
  public var code: String? = null,
  public var title: String? = null,
  public var detail: String? = null,
  public var source: JsonApiError.Source? = null,
  @PublishedApi internal var errorMeta: JsonObject? = null,
) {
  public fun links(links: JsonApiLinks): JsonApiErrorBuilder = this.apply {
    this.errorLinks = links
  }

  public fun links(builder: JsonApiLinksBuilder.() -> Unit): JsonApiErrorBuilder = this.apply {
    this.errorLinks = JsonApiLinksBuilder().apply(builder).build()
  }

  public fun meta(meta: JsonObject): JsonApiErrorBuilder = this.apply {
    this.errorMeta = meta
  }

  public inline fun meta(builder: JsonObjectBuilder.() -> Unit): JsonApiErrorBuilder = this.apply {
    this.errorMeta = buildJsonObject(builder)
  }

  @PublishedApi
  internal fun build(): JsonApiError =
    JsonApiError(
      id = id,
      links = errorLinks,
      status = status,
      code = code,
      title = title,
      detail = detail,
      source = source,
      meta = errorMeta,
    )
}
