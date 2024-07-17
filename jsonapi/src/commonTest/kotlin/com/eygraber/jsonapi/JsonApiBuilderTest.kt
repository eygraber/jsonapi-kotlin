package com.eygraber.jsonapi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class JsonApiBuilderTest {
  private val json = Json {
    prettyPrint = true
  }

  @Test
  fun single_resource() {
    val doc =
      JsonApiDocument
        .builder()
        .meta {
          put("created", "2019-01-01T00:00:00Z")
        }
        .jsonapi {
          version = "1.1"

          meta {
            put("created", "2019-01-01T00:00:00Z")
          }
        }
        .links {
          self = "http://example.com/articles/1"
          related("http://example.com/comments/5", serializeAsPrimitive = false)
          custom(
            "random",
            JsonApiLink(
              href = "http://example.com",
              title = "Random title",
            ),
          )
        }
        .resource(
          type = "articles",
          id = JsonApiId("1"),
          attributes = {
            put("title", "JSON:API paints my bikeshed!")
          },
        ) {
          relationships {
            relationship(named = "author") {
              links {
                self = "http://example.com/people/9"
              }

              meta {
                put("created", "2019-01-01T00:00:00Z")
              }

              toOne(
                type = "people",
                id = JsonApiId("9"),
              )
            }

            relationship(named = "comments") {
              toMany {
                addIdentifier(
                  type = "comments",
                  id = JsonApiId("5"),
                )

                addIdentifier(
                  type = "comments",
                  id = JsonApiId("12"),
                )
              }
            }
          }

          links {
            self = "http://example.com/articles/1"
          }

          meta {
            put("created", "2019-01-01T00:00:00Z")
          }
        }
        .addIncludedResource(
          type = "people",
          id = JsonApiId("9"),
          attributes = {
            put("firstName", "Dan")
            put("lastName", "Gebhardt")
            put("twitter", "dgeb")
          },
        ) {
          links {
            self = "http://example.com/people/9"
          }
        }
        .addIncludedResource(
          type = "comments",
          id = JsonApiId("5"),
          attributes = {
            put("body", "First!")
          },
        )
        .addIncludedResource(
          type = "comments",
          id = JsonApiId("12"),
          attributes = {
            put("body", "I like XML better")
          },
        )
        .build()

    assertEquals(
      expected = SingleResourceJson,
      actual = json.encodeToString(doc),
    )
  }

  @Test
  fun multiple_resources() {
    val doc =
      JsonApiDocument
        .builder()
        .resources {
          addResource(
            type = "articles",
            id = JsonApiId("1"),
            attributes = {
              put("title", "JSON:API paints my bikeshed!")
            },
          ) {
            relationships {
              relationship(named = "author") {
                toOne(
                  type = "people",
                  id = JsonApiId("9"),
                )
              }

              relationship(named = "comments") {
                toMany {
                  addIdentifier(
                    type = "comments",
                    id = JsonApiId("5"),
                  )

                  addIdentifier(
                    type = "comments",
                    id = JsonApiId("12"),
                  )
                }
              }
            }
          }

          addResource(
            type = "articles",
            id = JsonApiId("2"),
            attributes = {
              put("title", "Rails is Omakase")
            },
          ) {
            relationships {
              relationship(named = "author") {
                toOne(
                  type = "people",
                  id = JsonApiId("9"),
                )
              }

              relationship(named = "comments") {
                toMany {
                  addIdentifier(
                    type = "comments",
                    id = JsonApiId("3"),
                  )

                  addIdentifier(
                    type = "comments",
                    id = JsonApiId("4"),
                  )
                }
              }
            }
          }
        }
        .addIncludedResource(
          type = "people",
          id = JsonApiId("9"),
          attributes = {
            put("firstName", "Dan")
            put("lastName", "Gebhardt")
            put("twitter", "dgeb")
          },
        )
        .addIncludedResource(
          type = "comments",
          id = JsonApiId("5"),
          attributes = {
            put("body", "First!")
          },
        )
        .addIncludedResource(
          type = "comments",
          id = JsonApiId("12"),
          attributes = {
            put("body", "I like XML better")
          },
        )
        .build()

    assertEquals(
      expected = MultipleResourcesJson,
      actual = json.encodeToString(doc),
    )
  }

  @Test
  fun single_resource_identifier() {
    val doc =
      JsonApiDocument
        .builder()
        .identifier(
          type = "articles",
          id = JsonApiId("1"),
        ) {
          meta {
            put("created", "2019-01-01T00:00:00Z")
          }
        }
        .build()

    assertEquals(
      expected = SingleResourceIdentifierJson,
      actual = json.encodeToString(doc),
    )
  }

  @Test
  fun multiple_resource_identifiers() {
    val doc =
      JsonApiDocument
        .builder()
        .identifiers {
          addIdentifier(
            type = "articles",
            id = JsonApiId("1"),
          )

          addIdentifier(
            type = "articles",
            id = JsonApiId("2"),
          )
        }
        .build()

    assertEquals(
      expected = MultipleResourceIdentifiersJson,
      actual = json.encodeToString(doc),
    )
  }

  @Test
  fun meta_only() {
    val doc =
      JsonApiDocument
        .builder()
        .meta {
          put("foo", "bar")
          put("baz", "bang")
        }
        .links {
          self = "http://example.com/foo/bar/baz/bang"
        }
        .buildMetaDocument()

    assertEquals(
      expected = MetaOnlyJson,
      actual = json.encodeToString(doc),
    )
  }

  @Test
  fun meta_only_fails_without_meta() {
    val error = assertFails {
      JsonApiDocument
        .builder()
        .links {
          self = "http://example.com/foo/bar/baz/bang"
        }
        .buildMetaDocument()
    }

    assertEquals(
      expected = "A meta hasn't been set on this builder",
      actual = error.message,
    )
  }

  @Test
  fun errors() {
    val doc =
      JsonApiDocument
        .builder()
        .buildErrorDocument {
          addError(
            status = "422",
            title = "Invalid Attribute",
            detail = "Title is required",
            source = JsonApiError.Source(
              pointer = "/data/attributes/title",
            ),
          )
        }

    assertEquals(
      expected = ErrorsPointerJson,
      actual = json.encodeToString(doc),
    )
  }

  @Test
  fun errors_with_meta() {
    val doc =
      JsonApiDocument
        .builder()
        .buildErrorDocument {
          addError(
            status = "422",
            title = "Invalid Attribute",
            detail = "Title is required",
            source = JsonApiError.Source(
              pointer = "/data/attributes/title",
            ),
          ) {
            meta {
              put("created", "2019-01-01T00:00:00Z")
            }
          }
        }

    assertEquals(
      expected = ErrorsWithMetaJson,
      actual = json.encodeToString(doc),
    )
  }
}
