package com.eygraber.jsonapi

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
import kotlin.jvm.JvmInline

@Serializable(with = JsonApiId.Serializer::class)
@JvmInline
public value class JsonApiId(public val id: String) {
  public val isSpecified: Boolean get() = this != NoId

  override fun toString(): String = id

  public object Serializer : KSerializer<JsonApiId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("JsonApiId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: JsonApiId) {
      require(encoder is JsonEncoder)

      when(value) {
        NoId -> encoder.encodeJsonElement(JsonNull)
        else -> encoder.encodeJsonElement(JsonPrimitive(value.id))
      }
    }

    override fun deserialize(decoder: Decoder): JsonApiId {
      require(decoder is JsonDecoder)
      return when(val jsonElement = decoder.decodeJsonElement().jsonPrimitive) {
        JsonNull -> NoId
        else -> JsonApiId(jsonElement.content)
      }
    }
  }

  public companion object {
    public val NoId: JsonApiId = JsonApiId("")
  }
}
