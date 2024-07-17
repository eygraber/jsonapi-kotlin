package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiId
import com.eygraber.jsonapi.JsonApiLinks
import com.eygraber.jsonapi.JsonApiRelationship
import com.eygraber.jsonapi.JsonApiResource
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject

public class JsonApiResourceBuilder(
  private val type: String,
  private val id: JsonApiId,
  private val attributes: JsonObject,
  public var lid: JsonApiId = JsonApiId.NoId,
  @PublishedApi internal var relationships: Map<String, JsonApiRelationship>? = null,
  @PublishedApi internal var links: JsonApiLinks? = null,
  @PublishedApi internal var meta: JsonObject? = null,
) {
  public inline fun relationships(
    builder: JsonApiRelationshipsBuilder.() -> Unit,
  ): JsonApiResourceBuilder = this.apply {
    relationships = JsonApiRelationshipsBuilder().apply(builder).build()
  }

  public fun links(links: JsonApiLinks): JsonApiResourceBuilder = this.apply {
    this.links = links
  }

  public fun links(builder: JsonApiLinksBuilder.() -> Unit): JsonApiResourceBuilder = this.apply {
    this.links = JsonApiLinksBuilder().apply(builder).build()
  }

  public fun meta(meta: JsonObject): JsonApiResourceBuilder = this.apply {
    this.meta = meta
  }

  public inline fun meta(builder: JsonObjectBuilder.() -> Unit): JsonApiResourceBuilder = this.apply {
    this.meta = buildJsonObject(builder)
  }

  public fun build(): JsonApiResource = JsonApiResource(
    type = type,
    id = id,
    lid = lid,
    attributes = attributes,
    relationships = relationships,
    links = links,
    meta = meta,
  )
}
