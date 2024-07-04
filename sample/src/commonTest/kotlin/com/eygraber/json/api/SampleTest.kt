package com.eygraber.json.api

import com.eygraber.json.api.sample.Bar
import com.eygraber.json.api.sample.Baz
import com.eygraber.json.api.sample.Boom
import com.eygraber.json.api.sample.Foo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

class JsonApiTest {
  @Test
  fun testObject() {
    val api = JsonApi(Json { prettyPrint = true })
    val foo = Foo(
      id = ResourceId("IAmLocal"),
      name = "Foo",
      bar = Bar(
        baz = Baz(
          boo = "boo",
        ),
        boom = Boom(
          boom = "boom",
        ),
      ),
    )
    println(api.json.encodeToString(api.buildDocumentFrom(foo)))
  }

  // @Test
  // fun testArray() {
  //   val api = JsonApi(Json { prettyPrint = true })
  //   val document = api.decodeArrayDocument(NetworkIncludedMockModel.arrayResponse)
  //   val resource = api.toResources<NetworkIncludedMockModel>(document)
  //   println(resource)
  // }
}
