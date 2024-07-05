package com.eygraber.jsonapi

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
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * https://jsonapi.org/format/#auto-id--link-objects
 */
@Serializable(with = JsonApiLink.Serializer::class)
public data class JsonApiLink(
  public val href: String,
  public val rel: String? = null,
  public val describedBy: JsonApiLink? = null,
  public val title: String? = null,
  public val type: String? = null,
  public val hrefLang: List<String>? = null,
  public val meta: JsonObject? = null,
  /**
   * Used to determine if this JsonApiLink was originally serialized as a primitive.
   * Serializing a JsonApiLink as a String is a compact way of storing a JsonApiLink that just has an [href].
   * If `true`, when this JsonApiLink is serialized it will be serialized as a primitive instead of an object.
   */
  private val shouldSerializeHrefAsPrimitive: Boolean = true,
) {
  internal object Serializer : KSerializer<JsonApiLink> {
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
      (encoder as JsonEncoder).encodeJsonElement(
        if(value.shouldSerializeHrefAsPrimitive && value.rel == null && value.describedBy == null &&
          value.title == null && value.type == null && value.hrefLang == null && value.meta == null
        ) {
          JsonPrimitive(value.href)
        }
        else {
          buildJsonObject {
            put("href", value.href)
            value.rel?.let { put("rel", it) }
            value.describedBy?.let { put("describedBy", encoder.json.encodeToJsonElement(it)) }
            value.title?.let { put("title", it) }
            value.type?.let { put("type", it) }
            value.hrefLang?.let { put("hrefLang", encoder.json.encodeToJsonElement(it)) }
            value.meta?.let { put("meta", it) }
          }
        },
      )
    }

    override fun deserialize(decoder: Decoder): JsonApiLink {
      require(decoder is JsonDecoder)
      return when(val jsonElement = decoder.decodeJsonElement()) {
        is JsonPrimitive -> JsonApiLink(href = jsonElement.content, shouldSerializeHrefAsPrimitive = true)
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
            shouldSerializeHrefAsPrimitive = false,
          )
        }

        else -> throw SerializationException("Unexpected JSON element for JsonApiLink: $jsonElement")
      }
    }
  }
}
