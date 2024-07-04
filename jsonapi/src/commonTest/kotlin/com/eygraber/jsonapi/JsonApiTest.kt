package com.eygraber.jsonapi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class JsonApiTest {
  private val json = Json {
    prettyPrint = true
  }

  @Test
  fun single_resource() {
    val resource = assertComparisons<JsonApiDocument.Resource>(SingleResourceJson)

    assertEquals("1", resource.data.id)
    assertEquals("articles", resource.data.type)
    assertEquals("JSON:API paints my bikeshed!", resource.data.attributes?.get("title")?.jsonPrimitive?.content)

    val relationships = resource.data.relationships
    assertNotNull(relationships)

    val authorRelationship = relationships["author"]
    assertNotNull(authorRelationship)
    val authorRelationshipData = authorRelationship.data
    assertIs<JsonApiResourceLinkage.One>(authorRelationshipData)
    assertEquals("9", authorRelationshipData.data.id)
    assertEquals("people", authorRelationshipData.data.type)

    assertEquals("http://example.com/people/9", authorRelationship.links?.self?.href)
    assertEquals("2019-01-01T00:00:00Z", authorRelationship.meta?.get("created")?.jsonPrimitive?.contentOrNull)

    val commentRelationship = relationships["comments"]?.data
    assertIs<JsonApiResourceLinkage.Many>(commentRelationship)
    assertEquals(2, commentRelationship.data.size)

    val comment1 = commentRelationship.data.first()
    assertEquals("5", comment1.id)
    assertEquals("comments", comment1.type)

    val comment2 = commentRelationship.data[1]
    assertEquals("12", comment2.id)
    assertEquals("comments", comment2.type)

    assertEquals("http://example.com/articles/1", resource.data.links?.self?.href)
    assertEquals("2019-01-01T00:00:00Z", resource.data.meta?.get("created")?.jsonPrimitive?.contentOrNull)

    assertEquals("2019-01-01T00:00:00Z", resource.meta?.get("created")?.jsonPrimitive?.contentOrNull)
    assertEquals("http://example.com/articles/1", resource.links?.self?.href)
    assertEquals("http://example.com", resource.links?.additionalLinks?.get("random")?.href)
    assertEquals("Random title", resource.links?.additionalLinks?.get("random")?.title)

    val included = resource.included
    assertNotNull(included)
    assertEquals(3, included.size)

    val included1 = included.find(id = "9", type = "people")
    assertNotNull(included1)
    assertEquals("9", included1.id)
    assertEquals("people", included1.type)
    assertEquals("Dan", included1.attributes?.get("firstName")?.jsonPrimitive?.contentOrNull)
    assertEquals("Gebhardt", included1.attributes?.get("lastName")?.jsonPrimitive?.contentOrNull)
    assertEquals("dgeb", included1.attributes?.get("twitter")?.jsonPrimitive?.contentOrNull)
    assertEquals("http://example.com/people/9", included1.links?.self?.href)

    val included2 = included.find(id = "5", type = "comments")
    assertNotNull(included2)
    assertEquals("5", included2.id)
    assertEquals("comments", included2.type)
    assertEquals("First!", included2.attributes?.get("body")?.jsonPrimitive?.contentOrNull)

    val included3 = included.find(id = "12", type = "comments")
    assertNotNull(included3)
    assertEquals("12", included3.id)
    assertEquals("comments", included3.type)
    assertEquals("I like XML better", included3.attributes?.get("body")?.jsonPrimitive?.contentOrNull)
  }

  @Test
  fun multiple_resources() {
    val resources = assertComparisons<JsonApiDocument.Resources>(MultipleResourcesJson)

    assertEquals(2, resources.data.size)

    val data1 = resources.data.first()

    assertEquals("1", data1.id)
    assertEquals("articles", data1.type)
    assertEquals("JSON:API paints my bikeshed!", data1.attributes?.get("title")?.jsonPrimitive?.content)

    val data2 = resources.data[1]

    assertEquals("2", data2.id)
    assertEquals("articles", data2.type)
    assertEquals("Rails is Omakase", data2.attributes?.get("title")?.jsonPrimitive?.content)

    val included = resources.included
    assertNotNull(included)
    assertEquals(3, included.size)

    val included1 = included.find(id = "9", type = "people")
    assertNotNull(included1)
    assertEquals("9", included1.id)
    assertEquals("people", included1.type)
    assertEquals("Dan", included1.attributes?.get("firstName")?.jsonPrimitive?.contentOrNull)
    assertEquals("Gebhardt", included1.attributes?.get("lastName")?.jsonPrimitive?.contentOrNull)
    assertEquals("dgeb", included1.attributes?.get("twitter")?.jsonPrimitive?.contentOrNull)

    val included2 = included.find(id = "5", type = "comments")
    assertNotNull(included2)
    assertEquals("5", included2.id)
    assertEquals("comments", included2.type)
    assertEquals("First!", included2.attributes?.get("body")?.jsonPrimitive?.contentOrNull)

    val included3 = included.find(id = "12", type = "comments")
    assertNotNull(included3)
    assertEquals("12", included3.id)
    assertEquals("comments", included3.type)
    assertEquals("I like XML better", included3.attributes?.get("body")?.jsonPrimitive?.contentOrNull)
  }

  @Test
  fun single_resource_identifier() {
    val resourceIdentifier = assertComparisons<JsonApiDocument.Identifier>(SingleResourceIdentifierJson)

    assertEquals("1", resourceIdentifier.data.id)
    assertEquals("articles", resourceIdentifier.data.type)
    assertEquals("2019-01-01T00:00:00Z", resourceIdentifier.data.meta?.get("created")?.jsonPrimitive?.contentOrNull)
  }

  @Test
  fun multiple_resource_identifiers() {
    val resourceIdentifiers = assertComparisons<JsonApiDocument.Identifiers>(MultipleResourceIdentifiersJson)

    assertEquals(2, resourceIdentifiers.data.size)

    val data1 = resourceIdentifiers.data.first()

    assertEquals("1", data1.id)
    assertEquals("articles", data1.type)

    val data2 = resourceIdentifiers.data[1]

    assertEquals("2", data2.id)
    assertEquals("articles", data2.type)
  }

  @Test
  fun meta_only() {
    val meta = assertComparisons<JsonApiDocument.Meta>(MetaOnlyJson)
    assertEquals("bar", meta.meta["foo"]?.jsonPrimitive?.contentOrNull)
    assertEquals("bang", meta.meta["baz"]?.jsonPrimitive?.contentOrNull)
    assertEquals("http://example.com/foo/bar/baz/bang", meta.links?.self?.href)
  }

  @Test
  fun errors_with_pointer() {
    val errorsDocument = assertComparisons<JsonApiDocument.Errors>(ErrorsPointerJson)
    val errors = errorsDocument.errors

    assertEquals(1, errors.size)
    assertEquals("422", errors.first().status)
    assertEquals("Invalid Attribute", errors.first().title)
    assertEquals("Title is required", errors.first().detail)
    assertEquals("/data/attributes/title", errors.first().source?.pointer)
  }

  @Test
  fun errors_with_parameter() {
    val errorsDocument = assertComparisons<JsonApiDocument.Errors>(ErrorsParameterJson)
    val errors = errorsDocument.errors

    assertEquals(1, errors.size)
    assertEquals("422", errors.first().status)
    assertEquals("Invalid Attribute", errors.first().title)
    assertEquals("Title is required", errors.first().detail)
    assertEquals("title", errors.first().source?.parameter)
  }

  @Test
  fun errors_with_header() {
    val errorsDocument = assertComparisons<JsonApiDocument.Errors>(ErrorsHeaderJson)
    val errors = errorsDocument.errors

    assertEquals(1, errors.size)
    assertEquals("422", errors.first().status)
    assertEquals("Invalid Attribute", errors.first().title)
    assertEquals("Title is required", errors.first().detail)
    assertEquals("title", errors.first().source?.header)
  }

  @Test
  fun errors_with_more_than_one_property_in_source_throws() {
    val throwable = assertFails {
      json.decodeFromString<JsonApiDocument>(ErrorsWithPointerAndParameterJson)
    }

    assertIs<IllegalStateException>(throwable)
    assertEquals(
      expected = "Source should only have one of pointer, parameter, and header",
      actual = throwable.message,
    )
  }

  @Test
  fun document_with_data_and_error_fails() {
    val throwable = assertFails {
      json.decodeFromString<JsonApiDocument>(InvalidDocumentDataAndErrors)
    }

    assertIs<RuntimeException>(throwable)
    assertEquals(
      expected = "\"data\" and \"errors\" can't both be present at the top level of a JSON:API document",
      actual = throwable.message,
    )
  }

  @Test
  fun document_with_included_and_not_data_fails() {
    val throwable = assertFails {
      json.decodeFromString<JsonApiDocument>(InvalidDocumentIncludedResourcesWithoutData)
    }

    assertIs<RuntimeException>(throwable)
    assertEquals(
      expected = "\"included\" can't be present at the top level of a JSON:API document without \"data\" being present",
      actual = throwable.message,
    )
  }

  private inline fun <reified T : Any> assertComparisons(fixture: String): T {
    val document = json.decodeFromString<JsonApiDocument>(fixture)
    val subject = json.decodeFromString<T>(fixture)

    val decodedDocumentElement = json.encodeToJsonElement(document)
    val decodedResourceElement = json.encodeToJsonElement(subject)

    val decodedDocumentString = json.encodeToString(document)
    val decodedResourceString = json.encodeToString(subject)

    val fromJson = json.decodeFromJsonElement<JsonApiDocument>(decodedDocumentElement)

    assertIs<T>(document)
    assertEquals(document, subject)
    assertEquals(document, fromJson)

    assertEquals(decodedDocumentElement, decodedResourceElement)

    assertEquals(fixture, decodedDocumentString)
    assertEquals(decodedDocumentString, decodedResourceString)

    return subject
  }
}
