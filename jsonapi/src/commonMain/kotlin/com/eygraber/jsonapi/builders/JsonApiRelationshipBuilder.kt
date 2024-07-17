package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiId
import com.eygraber.jsonapi.JsonApiLinks
import com.eygraber.jsonapi.JsonApiRelationship
import com.eygraber.jsonapi.JsonApiResourceIdentifier
import com.eygraber.jsonapi.JsonApiResourceLinkage
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject

public class JsonApiRelationshipBuilder(
  @PublishedApi internal var relationshipLinks: JsonApiLinks? = null,
  @PublishedApi internal var relationshipMeta: JsonObject? = null,
) {
  public fun links(links: JsonApiLinks): JsonApiRelationshipBuilder = this.apply {
    this.relationshipLinks = links
  }

  public fun links(builder: JsonApiLinksBuilder.() -> Unit): JsonApiRelationshipBuilder = this.apply {
    this.relationshipLinks = JsonApiLinksBuilder().apply(builder).build()
  }

  public fun meta(meta: JsonObject): JsonApiRelationshipBuilder = this.apply {
    this.relationshipMeta = meta
  }

  public inline fun meta(builder: JsonObjectBuilder.() -> Unit): JsonApiRelationshipBuilder = this.apply {
    this.relationshipMeta = buildJsonObject(builder)
  }

  public fun toOne(): JsonApiRelationship =
    JsonApiRelationship(
      links = relationshipLinks,
      data = null,
      meta = relationshipMeta,
    )

  public fun toMany(): JsonApiRelationship =
    JsonApiRelationship(
      links = relationshipLinks,
      data = JsonApiResourceLinkage.Many(emptyList()),
      meta = relationshipMeta,
    )

  public inline fun toOne(
    type: String,
    id: JsonApiId,
    lid: JsonApiId = JsonApiId.NoId,
    meta: JsonObject? = null,
    builder: JsonApiResourceIdentifierBuilder.() -> Unit = {},
  ): JsonApiRelationship =
    JsonApiRelationship(
      links = relationshipLinks,
      data = JsonApiResourceLinkage.One(
        JsonApiResourceIdentifierBuilder(
          type = type,
          id = id,
          lid = lid,
          meta = meta,
        ).apply(builder).build(),
      ),
      meta = relationshipMeta,
    )

  public fun toMany(
    data: List<JsonApiResourceIdentifier>,
  ): JsonApiRelationship =
    JsonApiRelationship(
      links = relationshipLinks,
      data = JsonApiResourceLinkage.Many(data),
      meta = relationshipMeta,
    )

  public inline fun toMany(
    builder: JsonApiResourceIdentifiersBuilder.() -> Unit,
  ): JsonApiRelationship =
    JsonApiRelationship(
      links = relationshipLinks,
      data = JsonApiResourceLinkage.Many(
        JsonApiResourceIdentifiersBuilder().apply(builder).build(),
      ),
      meta = relationshipMeta,
    )
}
