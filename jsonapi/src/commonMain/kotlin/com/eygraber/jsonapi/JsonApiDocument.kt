package com.eygraber.jsonapi

import com.eygraber.jsonapi.builders.JsonApiDocumentBuilder
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

  public sealed interface IdentifierOrErrors
  public sealed interface IdentifiersOrErrors
  public sealed interface ResourceOrErrors
  public sealed interface ResourcesOrErrors
  public sealed interface MetaOrErrors

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
  ) : Data(links), IdentifierOrErrors

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Identifiers(
    public val data: List<JsonApiResourceIdentifier>,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data(links), IdentifiersOrErrors

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Resource(
    public val data: JsonApiResource,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data(links), ResourceOrErrors

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Resources(
    public val data: List<JsonApiResource>,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
    public override val included: List<JsonApiResource>? = null,
  ) : Data(links), ResourcesOrErrors

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Meta(
    public override val meta: JsonObject,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
  ) : JsonApiDocument, MetaOrErrors

  @Serializable(with = JsonApiDocumentSerializer::class)
  public data class Errors(
    public val errors: List<JsonApiError>,
    public override val meta: JsonObject? = null,
    public override val jsonapi: JsonApiObject? = null,
    public override val links: JsonApiLinks? = null,
  ) : JsonApiDocument, IdentifierOrErrors, IdentifiersOrErrors, ResourceOrErrors, ResourcesOrErrors, MetaOrErrors {
    public val aboutLink: JsonApiLink? = links?.additionalLinks?.get("about")
    public val type: JsonApiLink? = links?.additionalLinks?.get("type")
  }

  public companion object {
    public fun builder(
      meta: JsonObject? = null,
      jsonapi: JsonApiObject? = null,
      links: JsonApiLinks? = null,
    ): JsonApiDocumentBuilder = JsonApiDocumentBuilder(meta, jsonapi, links)
  }
}

public val JsonApiDocument.asIdentifier: JsonApiDocument.Identifier
  get() {
    check(this is JsonApiDocument.Identifier) {
      "JsonApiDocument is not an Identifier; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asIdentifierOrNull: JsonApiDocument.Identifier?
  get() =
    when(this) {
      is JsonApiDocument.Identifier -> this
      else -> null
    }

public val JsonApiDocument.asIdentifierOrErrors: JsonApiDocument.IdentifierOrErrors
  get() {
    check(this is JsonApiDocument.IdentifierOrErrors) {
      "JsonApiDocument is not an Identifier or an Errors; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asIdentifierOrErrorsOrNull: JsonApiDocument.IdentifierOrErrors?
  get() =
    when(this) {
      is JsonApiDocument.IdentifierOrErrors -> this
      else -> null
    }

public val JsonApiDocument.asIdentifiers: JsonApiDocument.Identifiers
  get() {
    check(this is JsonApiDocument.Identifiers) {
      "JsonApiDocument is not an Identifiers; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asIdentifiersOrNull: JsonApiDocument.Identifiers?
  get() =
    when(this) {
      is JsonApiDocument.Identifiers -> this
      else -> null
    }

public val JsonApiDocument.asIdentifiersOrErrors: JsonApiDocument.IdentifiersOrErrors
  get() {
    check(this is JsonApiDocument.IdentifiersOrErrors) {
      "JsonApiDocument is not an Identifiers or an Errors; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asIdentifiersOrErrorsOrNull: JsonApiDocument.IdentifiersOrErrors?
  get() =
    when(this) {
      is JsonApiDocument.IdentifiersOrErrors -> this
      else -> null
    }

public val JsonApiDocument.asResource: JsonApiDocument.Resource
  get() {
    check(this is JsonApiDocument.Resource) {
      "JsonApiDocument is not a Resource; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asResourceOrNull: JsonApiDocument.Resource?
  get() =
    when(this) {
      is JsonApiDocument.Resource -> this
      else -> null
    }

public val JsonApiDocument.asResourceOrErrors: JsonApiDocument.ResourceOrErrors
  get() {
    check(this is JsonApiDocument.ResourceOrErrors) {
      "JsonApiDocument is not a Resource or an Errors; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asResourceOrErrorsOrNull: JsonApiDocument.ResourceOrErrors?
  get() =
    when(this) {
      is JsonApiDocument.ResourceOrErrors -> this
      else -> null
    }

public val JsonApiDocument.asResources: JsonApiDocument.Resources
  get() {
    check(this is JsonApiDocument.Resources) {
      "JsonApiDocument is not a Resources; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asResourcesOrNull: JsonApiDocument.Resources?
  get() =
    when(this) {
      is JsonApiDocument.Resources -> this
      else -> null
    }

public val JsonApiDocument.asResourcesOrErrors: JsonApiDocument.ResourcesOrErrors
  get() {
    check(this is JsonApiDocument.ResourcesOrErrors) {
      "JsonApiDocument is not a Resources or an Errors; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asResourcesOrErrorsOrNull: JsonApiDocument.ResourcesOrErrors?
  get() =
    when(this) {
      is JsonApiDocument.ResourcesOrErrors -> this
      else -> null
    }

public val JsonApiDocument.asMeta: JsonApiDocument.Meta
  get() {
    check(this is JsonApiDocument.Meta) {
      "JsonApiDocument is not a Meta; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asMetaOrNull: JsonApiDocument.Meta?
  get() =
    when(this) {
      is JsonApiDocument.Meta -> this
      else -> null
    }

public val JsonApiDocument.asMetaOrErrors: JsonApiDocument.MetaOrErrors
  get() {
    check(this is JsonApiDocument.MetaOrErrors) {
      "JsonApiDocument is not a Meta or an Errors; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asMetaOrErrorsOrNull: JsonApiDocument.MetaOrErrors?
  get() =
    when(this) {
      is JsonApiDocument.MetaOrErrors -> this
      else -> null
    }

public val JsonApiDocument.asErrors: JsonApiDocument.Errors
  get() {
    check(this is JsonApiDocument.Errors) {
      "JsonApiDocument is not an Errors; is ${this::class.simpleName}"
    }
    return this
  }

public val JsonApiDocument.asErrorsOrNull: JsonApiDocument.Errors?
  get() =
    when(this) {
      is JsonApiDocument.Errors -> this
      else -> null
    }
