package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiDocument
import com.eygraber.jsonapi.JsonApiError
import com.eygraber.jsonapi.JsonApiId
import com.eygraber.jsonapi.JsonApiLinks
import com.eygraber.jsonapi.JsonApiObject
import com.eygraber.jsonapi.JsonApiRelationship
import com.eygraber.jsonapi.JsonApiResource
import com.eygraber.jsonapi.JsonApiResourceIdentifier
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject

public class JsonApiDocumentBuilder(
  @PublishedApi internal var documentMeta: JsonObject? = null,
  @PublishedApi internal var documentJsonapi: JsonApiObject? = null,
  @PublishedApi internal var documentLinks: JsonApiLinks? = null,
) {
  public fun meta(meta: JsonObject): JsonApiDocumentBuilder = this.apply {
    documentMeta = meta
  }

  public inline fun meta(builder: JsonObjectBuilder.() -> Unit): JsonApiDocumentBuilder = this.apply {
    documentMeta = buildJsonObject(builder)
  }

  public fun jsonapi(jsonapi: JsonApiObject): JsonApiDocumentBuilder = this.apply {
    documentJsonapi = jsonapi
  }

  public fun jsonapi(builder: JsonApiObjectBuilder.() -> Unit): JsonApiDocumentBuilder = this.apply {
    documentJsonapi = JsonApiObjectBuilder().apply(builder).build()
  }

  public fun links(links: JsonApiLinks): JsonApiDocumentBuilder = this.apply {
    documentLinks = links
  }

  public fun links(builder: JsonApiLinksBuilder.() -> Unit): JsonApiDocumentBuilder = this.apply {
    documentLinks = JsonApiLinksBuilder().apply(builder).build()
  }

  public inline fun identifier(
    type: String,
    id: JsonApiId,
    lid: JsonApiId = JsonApiId.NoId,
    meta: JsonObject? = null,
    crossinline builder: JsonApiResourceIdentifierBuilder.() -> Unit = {},
  ): JsonApiDataDocumentBuilder<JsonApiDocument.Identifier> =
    JsonApiDataDocumentBuilder(
      dataBuilder = { includedResources ->
        JsonApiDocument.Identifier(
          data = JsonApiResourceIdentifierBuilder(
            type = type,
            id = id,
            lid = lid,
            meta = meta,
          ).apply(builder).build(),
          meta = documentMeta,
          jsonapi = documentJsonapi,
          links = documentLinks,
          included = includedResources,
        )
      },
    )

  public fun identifiers(
    data: List<JsonApiResourceIdentifier>,
  ): JsonApiDataDocumentBuilder<JsonApiDocument.Identifiers> =
    JsonApiDataDocumentBuilder(
      dataBuilder = { includedResources ->
        JsonApiDocument.Identifiers(
          data = data,
          meta = documentMeta,
          jsonapi = documentJsonapi,
          links = documentLinks,
          included = includedResources,
        )
      },
    )

  public inline fun identifiers(
    crossinline builder: JsonApiResourceIdentifiersBuilder.() -> Unit,
  ): JsonApiDataDocumentBuilder<JsonApiDocument.Identifiers> =
    JsonApiDataDocumentBuilder(
      dataBuilder = { includedResources ->
        JsonApiDocument.Identifiers(
          data = JsonApiResourceIdentifiersBuilder().apply(builder).build(),
          meta = documentMeta,
          jsonapi = documentJsonapi,
          links = documentLinks,
          included = includedResources,
        )
      },
    )

  public inline fun resource(
    type: String,
    id: JsonApiId,
    crossinline attributes: JsonObjectBuilder.() -> Unit,
    lid: JsonApiId = JsonApiId.NoId,
    relationships: Map<String, JsonApiRelationship>? = null,
    links: JsonApiLinks? = null,
    meta: JsonObject? = null,
    crossinline builder: JsonApiResourceBuilder.() -> Unit = {},
  ): JsonApiDataDocumentBuilder<JsonApiDocument.Resource> =
    resource(
      type = type,
      id = id,
      lid = lid,
      attributes = buildJsonObject(attributes),
      relationships = relationships,
      links = links,
      meta = meta,
      builder = builder,
    )

  public inline fun resource(
    type: String,
    id: JsonApiId,
    attributes: JsonObject,
    lid: JsonApiId = JsonApiId.NoId,
    relationships: Map<String, JsonApiRelationship>? = null,
    links: JsonApiLinks? = null,
    meta: JsonObject? = null,
    crossinline builder: JsonApiResourceBuilder.() -> Unit = {},
  ): JsonApiDataDocumentBuilder<JsonApiDocument.Resource> =
    JsonApiDataDocumentBuilder { includedResources ->
      JsonApiDocument.Resource(
        data = JsonApiResourceBuilder(
          type = type,
          id = id,
          lid = lid,
          attributes = attributes,
          relationships = relationships,
          links = links,
          meta = meta,
        ).apply(builder).build(),
        meta = documentMeta,
        jsonapi = documentJsonapi,
        links = documentLinks,
        included = includedResources,
      )
    }

  public fun resources(
    data: List<JsonApiResource>,
  ): JsonApiDataDocumentBuilder<JsonApiDocument.Resources> =
    JsonApiDataDocumentBuilder(
      dataBuilder = { includedResources ->
        JsonApiDocument.Resources(
          data = data,
          meta = documentMeta,
          jsonapi = documentJsonapi,
          links = documentLinks,
          included = includedResources,
        )
      },
    )

  public inline fun resources(
    crossinline builder: JsonApiResourcesBuilder.() -> Unit,
  ): JsonApiDataDocumentBuilder<JsonApiDocument.Resources> =
    JsonApiDataDocumentBuilder(
      dataBuilder = { includedResources ->
        JsonApiDocument.Resources(
          data = JsonApiResourcesBuilder().apply(builder).build(),
          meta = documentMeta,
          jsonapi = documentJsonapi,
          links = documentLinks,
          included = includedResources,
        )
      },
    )

  public fun buildMetaDocument(): JsonApiDocument.Meta =
    JsonApiDocument.Meta(
      meta = requireNotNull(documentMeta) {
        "A meta hasn't been set on this builder"
      },
      jsonapi = documentJsonapi,
      links = documentLinks,
    )

  public fun buildErrorDocument(
    errors: List<JsonApiError>,
  ): JsonApiDocument.Errors =
    JsonApiDocument.Errors(
      errors = errors,
      meta = documentMeta,
      jsonapi = documentJsonapi,
      links = documentLinks,
    )

  public inline fun buildErrorDocument(
    crossinline builder: JsonApiErrorsBuilder.() -> Unit,
  ): JsonApiDocument.Errors =
    JsonApiDocument.Errors(
      errors = JsonApiErrorsBuilder().apply(builder).build(),
      meta = documentMeta,
      jsonapi = documentJsonapi,
      links = documentLinks,
    )
}
