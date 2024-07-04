package com.eygraber.jsonapi

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject

@Serializable(with = JsonApiResourceLinkage.Serializer::class)
public sealed class JsonApiResourceLinkage {
  @Serializable
  @SerialName("One")
  public data class One(
    val data: JsonApiResourceIdentifier,
  ) : JsonApiResourceLinkage()

  @Serializable
  @SerialName("Many")
  public data class Many(
    val data: List<JsonApiResourceIdentifier>,
  ) : JsonApiResourceLinkage()

  internal object Serializer : KSerializer<JsonApiResourceLinkage> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("JsonApiResourceLinkage") {
      element<JsonApiResourceIdentifier>("One", isOptional = true)
      element<List<JsonApiResourceIdentifier>>("Many", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: JsonApiResourceLinkage) {
      when(value) {
        is One -> encoder.encodeSerializableValue(
          serializer = JsonApiResourceIdentifier.serializer(),
          value = value.data,
        )

        is Many -> encoder.encodeSerializableValue(
          serializer = ListSerializer(JsonApiResourceIdentifier.serializer()),
          value = value.data,
        )
      }
    }

    override fun deserialize(decoder: Decoder): JsonApiResourceLinkage {
      require(decoder is JsonDecoder)
      return when(val jsonElement = decoder.decodeJsonElement()) {
        is JsonObject -> One(
          decoder.json.decodeFromJsonElement(
            deserializer = JsonApiResourceIdentifier.serializer(),
            element = jsonElement,
          ),
        )

        is JsonArray -> Many(
          decoder.json.decodeFromJsonElement(
            deserializer = ListSerializer(JsonApiResourceIdentifier.serializer()),
            element = jsonElement,
          ),
        )

        else -> throw SerializationException(
          "Unexpected JSON element for JsonApiResourceLinkage: $jsonElement",
        )
      }
    }
  }
}
