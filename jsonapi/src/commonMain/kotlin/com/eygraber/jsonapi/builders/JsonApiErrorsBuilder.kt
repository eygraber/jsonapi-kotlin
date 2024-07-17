package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiError
import com.eygraber.jsonapi.JsonApiLinks
import kotlinx.serialization.json.JsonObject

public class JsonApiErrorsBuilder @PublishedApi internal constructor() {
  @PublishedApi internal val errors: MutableList<JsonApiError> = mutableListOf()

  public inline fun addError(
    id: String? = null,
    links: JsonApiLinks? = null,
    status: String? = null,
    code: String? = null,
    title: String? = null,
    detail: String? = null,
    source: JsonApiError.Source? = null,
    meta: JsonObject? = null,
    builder: JsonApiErrorBuilder.() -> Unit = {},
  ) {
    errors += JsonApiErrorBuilder(
      id = id,
      errorLinks = links,
      status = status,
      code = code,
      title = title,
      detail = detail,
      source = source,
      errorMeta = meta,
    ).apply(builder).build()
  }

  @PublishedApi
  internal fun build(): List<JsonApiError> = errors
}
