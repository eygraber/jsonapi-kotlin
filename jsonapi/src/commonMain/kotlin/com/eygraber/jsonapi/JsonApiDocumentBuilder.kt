package com.eygraber.jsonapi

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

public class KeyValueMapping {
  private val map = mutableMapOf<String, String>()

  public infix fun String.to(value: String) {
    map[this] = value
  }
}

public class JsonApiRelationshipBuilder {
  private val mutableRelationships = mutableMapOf<String, JsonApiRelationship>()
  public val relationships: Map<String, JsonApiRelationship> get() = mutableRelationships

  public fun emptyToOneRelationship(
    name: String,
    links: JsonApiLinks? = null,
    meta: JsonObject? = null,
  ) {
    mutableRelationships[name] = JsonApiRelationship(
      links = links,
      data = null,
      meta = meta,
    )
  }

  public fun emptyToManyRelationship(
    name: String,
    links: JsonApiLinks? = null,
    meta: JsonObject? = null,
  ) {
    mutableRelationships[name] = JsonApiRelationship(
      links = links,
      data = JsonApiResourceLinkage.Many(emptyList()),
      meta = meta,
    )
  }

  public fun singleToOneRelationship(
    name: String,
    links: JsonApiLinks? = null,
    data: JsonApiResourceIdentifier,
    meta: JsonObject? = null,
  ) {
    mutableRelationships[name] = JsonApiRelationship(
      links = links,
      data = JsonApiResourceLinkage.One(data),
      meta = meta,
    )
  }

  public fun singleToManyRelationship(
    name: String,
    links: JsonApiLinks? = null,
    data: List<JsonApiResourceIdentifier>,
    meta: JsonObject? = null,
  ) {
    mutableRelationships[name] = JsonApiRelationship(
      links = links,
      data = JsonApiResourceLinkage.Many(data),
      meta = meta,
    )
  }
}

public class JsonApiResourceBuilder(
  private val id: String?,
  private val lid: String? = null,
  private val type: String,
  private val attributes: JsonObject
) {
  public inline fun relationships(
    builder: JsonApiRelationshipBuilder.() -> Unit
  ) {
    val relationshipsBuilder = JsonApiRelationshipBuilder()
    relationshipsBuilder.builder()
    val relationships = relationshipsBuilder.relationships
  }
}

public class JsonApiDocumentBuilder {

  public fun addMainResource() {
    JsonApiDocument.Resource(
      data = JsonApiResource(
        id = "id",
        lid = "lid",
        type = "foo",
        attributes = buildJsonObject {

        },
        relationships = mapOf(
          "bar" to JsonApiRelationship(
            links = JsonApiLinks(),
            data = JsonApiResourceLinkage.One(
              data = JsonApiResourceIdentifier(
                id = "id",
                lid = "lid",
                type = "bar",
                meta = buildJsonObject {}
              )
            )
          )
        ),
        links = JsonApiLinks(),
        meta = buildJsonObject {  },
      )
    )
  }

  public fun addIncludedResource() {

  }
}
