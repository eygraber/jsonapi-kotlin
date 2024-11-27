package com.eygraber.jsonapi

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject

internal object JsonApiDocumentSerializer : KSerializer<JsonApiDocument> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("JsonApiDocument") {
    element<JsonObject>("data", isOptional = true)
    element<JsonArray>("errors", isOptional = true)
    element<JsonElement>("meta", isOptional = true)
    element<JsonElement>("jsonapi", isOptional = true)
    element<JsonObject>("links", isOptional = true)
    element<JsonArray>("included", isOptional = true)
  }

  override fun deserialize(decoder: Decoder): JsonApiDocument {
    val input = decoder as? JsonDecoder ?: throw SerializationException("Expected JsonInput for ${decoder::class}")
    val jsonObject = input.decodeJsonElement().jsonObject

    val hasData = "data" in jsonObject
    val hasErrors = "errors" in jsonObject

    if(hasData && hasErrors) {
      error("\"data\" and \"errors\" can't both be present at the top level of a JSON:API document")
    }

    if(!hasData && "included" in jsonObject) {
      error("\"included\" can't be present at the top level of a JSON:API document without \"data\" being present")
    }

    return when {
      hasData -> when(val data = jsonObject["data"]) {
        is JsonObject -> {
          val meta = jsonObject["meta"]?.jsonObject
          val jsonapi = jsonObject["jsonapi"]?.let { input.json.decodeFromJsonElement(JsonApiObject.serializer(), it) }

          val links = jsonObject["links"]?.let { links ->
            input.json.decodeFromJsonElement(JsonApiLinks.serializer(), links)
          }

          val included = jsonObject["included"]?.let { included ->
            input.json.decodeFromJsonElement(ListSerializer(JsonApiResource.serializer()), included)
          }

          if("attributes" in data) {
            JsonApiDocument.Resource(
              data = input.json.decodeFromJsonElement(JsonApiResource.serializer(), data),
              meta = meta,
              jsonapi = jsonapi,
              links = links,
              included = included,
            )
          } else {
            JsonApiDocument.Identifier(
              data = input.json.decodeFromJsonElement(JsonApiResourceIdentifier.serializer(), data),
              meta = meta,
              jsonapi = jsonapi,
              links = links,
              included = included,
            )
          }
        }

        is JsonArray -> {
          val meta = jsonObject["meta"]?.jsonObject
          val jsonapi = jsonObject["jsonapi"]?.let { input.json.decodeFromJsonElement(JsonApiObject.serializer(), it) }

          val links = jsonObject["links"]?.let { links ->
            input.json.decodeFromJsonElement(JsonApiLinks.serializer(), links)
          }

          val included = jsonObject["included"]?.let { included ->
            input.json.decodeFromJsonElement(ListSerializer(JsonApiResource.serializer()), included)
          }

          if(data.isEmpty() || "attributes" in data.first().jsonObject) {
            JsonApiDocument.Resources(
              data = data.map { input.json.decodeFromJsonElement(JsonApiResource.serializer(), it) },
              meta = meta,
              jsonapi = jsonapi,
              links = links,
              included = included,
            )
          } else {
            JsonApiDocument.Identifiers(
              data = data.map { input.json.decodeFromJsonElement(JsonApiResourceIdentifier.serializer(), it) },
              meta = meta,
              jsonapi = jsonapi,
              links = links,
              included = included,
            )
          }
        }

        else -> error("data must be an array or object")
      }

      hasErrors -> JsonApiDocument.Errors(
        errors = input.json.decodeFromJsonElement(
          deserializer = ListSerializer(JsonApiError.serializer()),
          element = requireNotNull(jsonObject["errors"]),
        ),
        meta = jsonObject["meta"]?.jsonObject,
        jsonapi = jsonObject["jsonapi"]?.let { input.json.decodeFromJsonElement(JsonApiObject.serializer(), it) },
        links = jsonObject["links"]?.let { input.json.decodeFromJsonElement(JsonApiLinks.serializer(), it) },
      )

      "meta" in jsonObject -> when(val meta = jsonObject["meta"]) {
        is JsonObject -> {
          JsonApiDocument.Meta(
            meta = meta,
            jsonapi = jsonObject["jsonapi"]?.let { input.json.decodeFromJsonElement(JsonApiObject.serializer(), it) },
            links = jsonObject["links"]?.let { input.json.decodeFromJsonElement(JsonApiLinks.serializer(), it) },
          )
        }

        else -> error("meta must be an object")
      }

      else -> throw SerializationException("Unknown JsonApiDocument type")
    }
  }

  override fun serialize(encoder: Encoder, value: JsonApiDocument) {
    val output = encoder as? JsonEncoder ?: throw SerializationException("Expected JsonOutput for ${encoder::class}")
    val jsonObject = buildJsonObject {
      when(value) {
        is JsonApiDocument.Data -> {
          when(value) {
            is JsonApiDocument.Identifier -> put(
              "data",
              output.json.encodeToJsonElement(
                JsonApiResourceIdentifier.serializer(),
                value.data,
              ),
            )

            is JsonApiDocument.Identifiers -> put(
              "data",
              buildJsonArray {
                value.data.forEach {
                  add(output.json.encodeToJsonElement(JsonApiResourceIdentifier.serializer(), it))
                }
              },
            )

            is JsonApiDocument.Resource -> put(
              "data",
              output.json.encodeToJsonElement(
                JsonApiResource.serializer(),
                value.data,
              ),
            )

            is JsonApiDocument.Resources -> put(
              "data",
              buildJsonArray {
                value.data.forEach {
                  add(output.json.encodeToJsonElement(JsonApiResource.serializer(), it))
                }
              },
            )
          }

          value.meta?.let { put("meta", it) }
          value.jsonapi?.let { put("jsonapi", output.json.encodeToJsonElement(JsonApiObject.serializer(), it)) }
          value.links?.let { links ->
            put(
              "links",
              output.json.encodeToJsonElement(JsonApiLinks.serializer(), links),
            )
          }
          value.included?.let { included ->
            put(
              "included",
              output.json.encodeToJsonElement(ListSerializer(JsonApiResource.serializer()), included),
            )
          }
        }

        is JsonApiDocument.Meta -> {
          put("meta", value.meta)
          value.jsonapi?.let { put("jsonapi", output.json.encodeToJsonElement(JsonApiObject.serializer(), it)) }
          value.links?.let { links ->
            put(
              "links",
              output.json.encodeToJsonElement(JsonApiLinks.serializer(), links),
            )
          }
        }

        is JsonApiDocument.Errors -> {
          put("errors", output.json.encodeToJsonElement(ListSerializer(JsonApiError.serializer()), value.errors))
          value.meta?.let { put("meta", it) }
          value.jsonapi?.let { put("jsonapi", output.json.encodeToJsonElement(JsonApiObject.serializer(), it)) }
          value.links?.let { put("links", output.json.encodeToJsonElement(JsonApiLinks.serializer(), it)) }
        }
      }
    }
    output.encodeJsonElement(jsonObject)
  }
}
