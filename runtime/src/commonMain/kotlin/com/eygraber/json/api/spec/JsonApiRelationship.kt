package com.eygraber.json.api.spec

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class JsonApiRelationship(
  public val links: JsonApiLinks? = null,
  @Serializable(with = JsonApiResourceLinkage.Serializer::class)
  public val data: JsonApiResourceLinkage? = null,
  public val meta: JsonObject? = null,
)
