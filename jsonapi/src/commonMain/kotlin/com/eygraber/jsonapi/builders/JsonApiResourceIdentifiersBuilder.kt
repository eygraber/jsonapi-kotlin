package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiId
import com.eygraber.jsonapi.JsonApiResourceIdentifier
import kotlinx.serialization.json.JsonObject

public class JsonApiResourceIdentifiersBuilder @PublishedApi internal constructor() {
  @PublishedApi internal val resourceIdentifiers: MutableList<JsonApiResourceIdentifier> = mutableListOf()

  public inline fun addIdentifier(
    type: String,
    id: JsonApiId,
    lid: JsonApiId = JsonApiId.NoId,
    meta: JsonObject? = null,
    builder: JsonApiResourceIdentifierBuilder.() -> Unit = {},
  ) {
    resourceIdentifiers += JsonApiResourceIdentifierBuilder(
      type = type,
      id = id,
      lid = lid,
      meta = meta,
    ).apply(builder).build()
  }

  @PublishedApi
  internal fun build(): List<JsonApiResourceIdentifier> = resourceIdentifiers
}
