package com.eygraber.json.api.spec

import com.eygraber.json.api.serializers.JsonApiDocumentSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
public sealed interface JsonApiDocument {
  public val meta: JsonObject?
  public val jsonapi: JsonApiObject?

  public sealed interface Data : JsonApiDocument {
    public val links: JsonApiLinks?
    public val included: List<JsonApiResource>?

    public val describedByLink: JsonApiLink? get() = links?.additionalLinks?.get("describedBy")
  }

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Identifier(
    public val data: JsonApiResourceIdentifier? = null,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Identifiers(
    public val data: List<JsonApiResourceIdentifier>? = null,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Resource(
    public val data: JsonApiResource? = null,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Resources(
    public val data: List<JsonApiResource>? = null,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Errors(
    public val errors: List<JsonApiError>,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public val links: JsonApiError.Links? = null,
  ) : JsonApiDocument
}
