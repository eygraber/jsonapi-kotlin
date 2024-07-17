package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiObject
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject

public class JsonApiObjectBuilder {
  @PublishedApi internal var meta: JsonObject? = null

  public var version: String? = null
  public var ext: List<String>? = null
  public var profile: List<String>? = null

  public fun meta(meta: JsonObject) {
    this.meta = meta
  }

  public inline fun meta(builder: JsonObjectBuilder.() -> Unit) {
    this.meta = buildJsonObject(builder)
  }

  internal fun build(): JsonApiObject = JsonApiObject(
    version = version,
    ext = ext,
    profile = profile,
    meta = meta,
  )
}
