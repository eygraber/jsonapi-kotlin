package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiId
import com.eygraber.jsonapi.JsonApiResourceIdentifier
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject

public class JsonApiResourceIdentifierBuilder(
  private val type: String,
  private val id: JsonApiId,
  public var lid: JsonApiId,
  @PublishedApi internal var meta: JsonObject?,
) {
  public fun meta(meta: JsonObject): JsonApiResourceIdentifierBuilder = this.apply {
    this.meta = meta
  }

  public inline fun meta(builder: JsonObjectBuilder.() -> Unit): JsonApiResourceIdentifierBuilder = this.apply {
    this.meta = buildJsonObject(builder)
  }

  public fun build(): JsonApiResourceIdentifier = JsonApiResourceIdentifier(
    id = id,
    lid = lid,
    type = type,
    meta = meta,
  )
}
