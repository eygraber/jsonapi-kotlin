package com.eygraber.json.api

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonApiTest {
  @Test
  fun testObject() {
    val api = JsonApi(Json { prettyPrint = true })
    val document = api.decodeArrayDocument(BlogJson)
    assertEquals(api.json.parseToJsonElement(BlogJson), api.json.encodeToJsonElement(document))
    assertEquals(document, api.decodeArrayDocument(api.json.encodeToString(document)))
  }
}
