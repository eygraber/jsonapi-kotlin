package com.eygraber.jsonapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * https://jsonapi.org/format/#document-top-level
 */
@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
@Serializable(with = JsonApiDocumentSerializer::class)
public sealed interface JsonApiDocument {
  public val meta: JsonObject?
  public val jsonapi: JsonApiObject?
  public val links: JsonApiLinks?

  public sealed class Data(links: JsonApiLinks?) : JsonApiDocument {
    public abstract val included: List<JsonApiResource>?

    public val describedByLink: JsonApiLink? = links?.additionalLinks?.get("describedBy")
  }

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Identifier(
    public val data: JsonApiResourceIdentifier,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data(links)

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Identifiers(
    public val data: List<JsonApiResourceIdentifier>,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data(links)

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Resource(
    public val data: JsonApiResource,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data(links)

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Resources(
    public val data: List<JsonApiResource>,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data(links)

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Meta(
    public override val meta: JsonObject,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
  ) : JsonApiDocument

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Errors(
    public val errors: List<JsonApiError>,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
  ) : JsonApiDocument {
    public val aboutLink: JsonApiLink? = links?.additionalLinks?.get("about")
    public val type: JsonApiLink? = links?.additionalLinks?.get("type")
  }
}
