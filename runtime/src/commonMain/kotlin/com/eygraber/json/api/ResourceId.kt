package com.eygraber.json.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = ResourceId.Serializer::class)
public class ResourceId(public val id: String) {
  public val isSpecified: Boolean get() = this != NoId

  override fun toString(): String = "ResourceId($id)"

  public object Serializer : KSerializer<ResourceId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ResourceId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ResourceId) {
      require(encoder is JsonEncoder)

      when(value) {
        NoId -> encoder.encodeJsonElement(JsonNull)
        else -> encoder.encodeJsonElement(JsonPrimitive(value.id))
      }
    }

    override fun deserialize(decoder: Decoder): ResourceId {
      require(decoder is JsonDecoder)
      return when(val jsonElement = decoder.decodeJsonElement().jsonPrimitive) {
        JsonNull -> NoId
        else -> ResourceId(jsonElement.content)
      }
    }
  }

  public companion object {
    public val NoId: ResourceId = ResourceId("")
  }
}
