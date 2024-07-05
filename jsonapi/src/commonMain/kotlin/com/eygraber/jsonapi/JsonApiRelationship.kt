package com.eygraber.jsonapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * https://jsonapi.org/format/#document-resource-object-relationships
 */
@Serializable
public data class JsonApiRelationship(
  public val links: JsonApiLinks? = null,
  @Serializable(with = JsonApiResourceLinkage.Serializer::class)
  public val data: JsonApiResourceLinkage? = null,
  public val meta: JsonObject? = null,
)
