package com.eygraber.json.api.spec

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

@Serializable
public data class JsonApiLinks(
  public val self: JsonApiLink? = null,
  public val related: JsonApiLink? = null,
  public val first: JsonApiLink? = null,
  public val last: JsonApiLink? = null,
  public val prev: JsonApiLink? = null,
  public val next: JsonApiLink? = null,
  public val additionalLinks: Map<String, JsonApiLink> = emptyMap(),
) {
  public object JsonApiLinksSerializer : KSerializer<JsonApiLinks> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("JsonApiLinks") {
      element<JsonApiLink>("self", isOptional = true)
      element<JsonApiLink>("related", isOptional = true)
      element<JsonApiLink>("first", isOptional = true)
      element<JsonApiLink>("last", isOptional = true)
      element<JsonApiLink>("prev", isOptional = true)
      element<JsonApiLink>("next", isOptional = true)
      element<Map<String, JsonApiLink>>("additionalLinks", isOptional = true)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: JsonApiLinks) {
      val compositeEncoder = encoder.beginStructure(descriptor)
      value.self?.let { compositeEncoder.encodeSerializableElement(descriptor, 0, JsonApiLink.serializer(), it) }
      value.related?.let { compositeEncoder.encodeSerializableElement(descriptor, 1, JsonApiLink.serializer(), it) }
      value.first?.let { compositeEncoder.encodeSerializableElement(descriptor, 2, JsonApiLink.serializer(), it) }
      value.last?.let { compositeEncoder.encodeSerializableElement(descriptor, 3, JsonApiLink.serializer(), it) }
      value.prev?.let { compositeEncoder.encodeSerializableElement(descriptor, 4, JsonApiLink.serializer(), it) }
      value.next?.let { compositeEncoder.encodeSerializableElement(descriptor, 5, JsonApiLink.serializer(), it) }

      value.additionalLinks.forEach { (key, link) ->
        compositeEncoder.encodeSerializableElement(
          descriptor,
          descriptor.getElementIndex(key),
          JsonApiLink.serializer(),
          link,
        )
      }
      compositeEncoder.endStructure(descriptor)
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
