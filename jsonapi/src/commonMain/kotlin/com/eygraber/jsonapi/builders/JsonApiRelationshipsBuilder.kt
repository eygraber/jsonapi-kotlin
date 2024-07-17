package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiLinks
import com.eygraber.jsonapi.JsonApiRelationship
import kotlinx.serialization.json.JsonObject

public class JsonApiRelationshipsBuilder {
  private val relationships = mutableMapOf<String, JsonApiRelationship>()

  public fun relationship(
    named: String,
    links: JsonApiLinks? = null,
    meta: JsonObject? = null,
    builder: JsonApiRelationshipBuilder.() -> JsonApiRelationship,
  ) {
    relationships[named] = JsonApiRelationshipBuilder(links, meta).run(builder)
  }

  @PublishedApi
  internal fun build(): Map<String, JsonApiRelationship> = relationships
}
