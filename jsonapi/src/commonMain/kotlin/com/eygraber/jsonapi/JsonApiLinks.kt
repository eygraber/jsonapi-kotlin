package com.eygraber.jsonapi

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = JsonApiLinks.Serializer::class)
public data class JsonApiLinks(
  public val self: JsonApiLink? = null,
  public val related: JsonApiLink? = null,
  public val first: JsonApiLink? = null,
  public val last: JsonApiLink? = null,
  public val prev: JsonApiLink? = null,
  public val next: JsonApiLink? = null,
  public val additionalLinks: Map<String, JsonApiLink> = emptyMap(),
) {
  internal object Serializer : KSerializer<JsonApiLinks> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("JsonApiLinks") {
      element<JsonApiLink>("self", isOptional = true)
      element<JsonApiLink>("related", isOptional = true)
      element<JsonApiLink>("first", isOptional = true)
      element<JsonApiLink>("last", isOptional = true)
      element<JsonApiLink>("prev", isOptional = true)
      element<JsonApiLink>("next", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: JsonApiLinks) {
      (encoder as JsonEncoder).encodeJsonElement(
        buildJsonObject {
          value.self?.let { put("self", encoder.json.encodeToJsonElement(it)) }
          value.related?.let { put("related", encoder.json.encodeToJsonElement(it)) }
          value.first?.let { put("first", encoder.json.encodeToJsonElement(it)) }
          value.last?.let { put("last", encoder.json.encodeToJsonElement(it)) }
          value.prev?.let { put("prev", encoder.json.encodeToJsonElement(it)) }
          value.next?.let { put("next", encoder.json.encodeToJsonElement(it)) }
          value.additionalLinks.forEach { (key, link) ->
            put(key, encoder.json.encodeToJsonElement(link))
          }
        },
      )
    }

    override fun deserialize(decoder: Decoder): JsonApiLinks {
      val input = decoder as? JsonDecoder ?: throw SerializationException("Expected Json input")
      val jsonObject = input.decodeJsonElement().jsonObject

      val self = jsonObject["self"]?.let { decoder.json.decodeFromJsonElement(JsonApiLink.serializer(), it) }
      val related = jsonObject["related"]?.let { decoder.json.decodeFromJsonElement(JsonApiLink.serializer(), it) }
      val first = jsonObject["first"]?.let { decoder.json.decodeFromJsonElement(JsonApiLink.serializer(), it) }
      val last = jsonObject["last"]?.let { decoder.json.decodeFromJsonElement(JsonApiLink.serializer(), it) }
      val prev = jsonObject["prev"]?.let { decoder.json.decodeFromJsonElement(JsonApiLink.serializer(), it) }
      val next = jsonObject["next"]?.let { decoder.json.decodeFromJsonElement(JsonApiLink.serializer(), it) }

      val knownKeys = setOf("self", "related", "first", "last", "prev", "next")
      val additionalLinks = jsonObject.filterKeys { it !in knownKeys }
        .mapValues { decoder.json.decodeFromJsonElement(JsonApiLink.serializer(), it.value) }

      return JsonApiLinks(
        self = self,
        related = related,
        first = first,
        last = last,
        prev = prev,
        next = next,
        additionalLinks = additionalLinks,
      )
    }
  }
}
