package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiId
import com.eygraber.jsonapi.JsonApiLinks
import com.eygraber.jsonapi.JsonApiRelationship
import com.eygraber.jsonapi.JsonApiResource
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject

public class JsonApiResourcesBuilder {
  @PublishedApi internal val resources: ArrayList<JsonApiResource> = ArrayList()

  public inline fun addResource(
    type: String,
    id: JsonApiId,
    crossinline attributes: JsonObjectBuilder.() -> Unit,
    lid: JsonApiId = JsonApiId.NoId,
    relationships: Map<String, JsonApiRelationship>? = null,
    links: JsonApiLinks? = null,
    meta: JsonObject? = null,
    crossinline builder: JsonApiResourceBuilder.() -> Unit = {},
  ): JsonApiResourcesBuilder = addResource(
    type = type,
    id = id,
    lid = lid,
    attributes = buildJsonObject(attributes),
    relationships = relationships,
    links = links,
    meta = meta,
    builder = builder,
  )

  public inline fun addResource(
    type: String,
    id: JsonApiId,
    attributes: JsonObject,
    lid: JsonApiId = JsonApiId.NoId,
    relationships: Map<String, JsonApiRelationship>? = null,
    links: JsonApiLinks? = null,
    meta: JsonObject? = null,
    crossinline builder: JsonApiResourceBuilder.() -> Unit = {},
  ): JsonApiResourcesBuilder = this.apply {
    resources += JsonApiResourceBuilder(
      type = type,
      id = id,
      lid = lid,
      attributes = attributes,
      relationships = relationships,
      links = links,
      meta = meta,
    ).apply(builder).build()
  }

  @PublishedApi internal fun build(): List<JsonApiResource> = resources
}
