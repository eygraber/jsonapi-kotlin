package com.eygraber.jsonapi.typed

import com.eygraber.jsonapi.JsonApiResource
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

public class JsonApiIncludedResources {
  private val resourceAttributesByIdAndType = mutableMapOf<Pair<String, String>, JsonElement?>()
  private val mutableIncludedResources = mutableListOf<JsonApiResource>()
  public val includedResources: List<JsonApiResource> = mutableIncludedResources

  public fun isAddToIncludedResourcesNeeded(
    id: JsonApiDocumentBuilder.Id,
    type: String,
    resourceAttributes: JsonObject,
  ): Boolean = resourceAttributes != resourceAttributesByIdAndType[id.idOrLid to type]

  public fun add(resource: JsonApiResource) {
    resourceAttributesByIdAndType[resource.requireIdOrLid() to resource.type] = resource.attributes
    mutableIncludedResources += resource
  }
}
