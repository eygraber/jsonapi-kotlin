package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiDocument
import com.eygraber.jsonapi.JsonApiId
import com.eygraber.jsonapi.JsonApiLinks
import com.eygraber.jsonapi.JsonApiRelationship
import com.eygraber.jsonapi.JsonApiResource
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject

public class JsonApiDataDocumentBuilder<T : JsonApiDocument.Data>(
  private val dataBuilder: (List<JsonApiResource>?) -> T,
) {
  @PublishedApi internal val includedResources: ArrayList<JsonApiResource> = ArrayList()

  public inline fun addIncludedResource(
    type: String,
    id: JsonApiId,
    crossinline attributes: JsonObjectBuilder.() -> Unit,
    lid: JsonApiId = JsonApiId.NoId,
    relationships: Map<String, JsonApiRelationship>? = null,
    links: JsonApiLinks? = null,
    meta: JsonObject? = null,
    crossinline builder: JsonApiResourceBuilder.() -> Unit = {},
  ): JsonApiDataDocumentBuilder<T> = addIncludedResource(
    type = type,
    id = id,
    lid = lid,
    attributes = buildJsonObject(attributes),
    relationships = relationships,
    links = links,
    meta = meta,
    builder = builder,
  )

  public inline fun addIncludedResource(
    type: String,
    id: JsonApiId,
    attributes: JsonObject,
    lid: JsonApiId = JsonApiId.NoId,
    relationships: Map<String, JsonApiRelationship>? = null,
    links: JsonApiLinks? = null,
    meta: JsonObject? = null,
    crossinline builder: JsonApiResourceBuilder.() -> Unit = {},
  ): JsonApiDataDocumentBuilder<T> = this.apply {
    includedResources += JsonApiResourceBuilder(
      type = type,
      id = id,
      lid = lid,
      attributes = attributes,
      relationships = relationships,
      links = links,
      meta = meta,
    ).apply(builder).build()
  }

  public fun build(): T = dataBuilder(includedResources.takeIf { it.isNotEmpty() })
}
