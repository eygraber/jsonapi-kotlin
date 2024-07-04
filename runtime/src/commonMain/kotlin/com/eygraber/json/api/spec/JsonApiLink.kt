package com.eygraber.json.api.spec

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = JsonApiLink.Serializer::class)
public data class JsonApiLink(
  public val href: String,
  public val rel: String? = null,
  public val describedBy: JsonApiLink? = null,
  public val title: String? = null,
  public val type: String? = null,
  public val hrefLang: List<String>? = null,
  public val meta: JsonObject? = null,
) {
  public object Serializer : KSerializer<JsonApiLink> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("JsonApiLink") {
      element<String>("href")
      element<String>("rel", isOptional = true)
      element<String>("describedBy", isOptional = true)
      element<String>("title", isOptional = true)
      element<String>("type", isOptional = true)
      element<String>("hrefLang", isOptional = true)
      element<JsonObject>("meta", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: JsonApiLink) {
      if (value.rel == null && value.describedBy == null && value.title == null &&
        value.type == null && value.hrefLang == null && value.meta == null) {
        encoder.encodeString(value.href)
      }
      else {
        val compositeEncoder = encoder.beginStructure(descriptor)
        compositeEncoder.encodeStringElement(descriptor, 0, value.href)
        value.rel?.let { compositeEncoder.encodeStringElement(descriptor, 1, it) }
        value.describedBy?.let { compositeEncoder.encodeSerializableElement(descriptor, 2, Serializer, it) }
        value.title?.let { compositeEncoder.encodeStringElement(descriptor, 3, it) }
        value.type?.let { compositeEncoder.encodeStringElement(descriptor, 4, it) }
        value.hrefLang?.let {
          compositeEncoder.encodeSerializableElement(
            descriptor,
            5,
            ListSerializer(String.serializer()),
            it,
          )
        }
        value.meta?.let { compositeEncoder.encodeSerializableElement(descriptor, 6, JsonObject.serializer(), it) }
        compositeEncoder.endStructure(descriptor)
      }
    }

    override fun deserialize(decoder: Decoder): JsonApiLink {
      require(decoder is JsonDecoder)
      return when(val jsonElement = decoder.decodeJsonElement()) {
        is JsonPrimitive -> JsonApiLink(href = jsonElement.content)
        is JsonObject -> {
          val jsonObject = jsonElement.jsonObject
          JsonApiLink(
            href = jsonObject["href"]?.jsonPrimitive?.content
              ?: throw SerializationException("Missing 'href' in JsonApiLink"),
            rel = jsonObject["rel"]?.jsonPrimitive?.contentOrNull,
            describedBy = jsonObject["describedBy"]?.let { decoder.json.decodeFromJsonElement(serializer(), it) },
            title = jsonObject["title"]?.jsonPrimitive?.contentOrNull,
            type = jsonObject["type"]?.jsonPrimitive?.contentOrNull,
            hrefLang = jsonObject["hrefLang"]?.let {
              decoder.json.decodeFromJsonElement(
                ListSerializer(String.serializer()),
                it,
              )
            },
            meta = jsonObject["meta"]?.jsonObject,
          )
        }

        else -> throw SerializationException("Unexpected JSON element for JsonApiLink: $jsonElement")
      }
    }
  }
}
