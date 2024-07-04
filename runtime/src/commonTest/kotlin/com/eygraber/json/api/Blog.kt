package com.eygraber.json.api

import com.eygraber.json.api.spec.JsonApiDocument
import com.eygraber.json.api.spec.JsonApiLinks
import com.eygraber.json.api.spec.JsonApiRelationship
import com.eygraber.json.api.spec.JsonApiResource
import com.eygraber.json.api.spec.JsonApiResourceIdentifier
import com.eygraber.json.api.spec.JsonApiResourceLinkage
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

@Serializable
data class Article(
  override val id: ResourceId,
  val title: String,
  val author: Person,
  val comments: List<Comment>,
) : JsonApi.Resource {
  fun toDoc() {
    JsonApiDocument.Resource(
      data = JsonApiResource(
        id = id.id,
        lid = lid.id,
        type = "articles",
        attributes = buildJsonObject {
          put("title", JsonPrimitive(title))
        },
        relationships = mapOf(
          "author" to JsonApiRelationship(
            data = JsonApiResourceLinkage.One(
              JsonApiResourceIdentifier(
                id = author.id.id,
                lid = author.lid.id,
                type = "people",
              ),
            ),
          ),
          "comments" to JsonApiRelationship(
            data = JsonApiResourceLinkage.Many(
              comments.map { comment ->
                JsonApiResourceIdentifier(
                  id = comment.id.id,
                  lid = comment.lid.id,
                  type = "comments",
                )
              },
            ),
          ),
        ),
      ),
      included = comments.map { comment ->
        JsonApiResource(
          id = comment.id.id,
          lid = comment.lid.id,
          type = "comments",
          attributes = buildJsonObject {
            put("body", JsonPrimitive(comment.body))
          },
          relationships = mapOf(
            "author" to JsonApiRelationship(
              data = JsonApiResourceLinkage.One(
                JsonApiResourceIdentifier(
                  id = comment.author.id.id,
                  lid = comment.author.lid.id,
                  type = "people",
                ),
              ),
            ),
          ),
          links = comment.links,
        )
      } + JsonApiResource(
        id = author.id.id,
        lid = author.lid.id,
        type = "people",
        attributes = buildJsonObject {
          put("firstName", JsonPrimitive(author.firstName))
          put("lastName", JsonPrimitive(author.lastName))
          put("twitter", JsonPrimitive(author.twitter))
        },
      ),
    )
  }
}

@Serializable
data class Person(
  override val id: ResourceId,
  val firstName: String,
  val lastName: String,
  val twitter: String,
) : JsonApi.Resource

@Serializable
data class Comment(
  override val id: ResourceId,
  val author: Person,
  val body: String,
  val links: JsonApiLinks? = null,
) : JsonApi.Resource

const val BlogJson = """
{
    "links": {
        "self": "http://example.com/articles",
        "next": "http://example.com/articles?page[offset]=2",
        "last": "http://example.com/articles?page[offset]=10"
    },
    "data": [
        {
            "type": "articles",
            "id": "1",
            "attributes": {
                "title": "JSON:API paints my bikeshed!"
            },
            "relationships": {
                "author": {
                    "links": {
                        "self": "http://example.com/articles/1/relationships/author",
                        "related": "http://example.com/articles/1/author"
                    },
                    "data": {
                        "type": "people",
                        "id": "9"
                    }
                },
                "comments": {
                    "links": {
                        "self": "http://example.com/articles/1/relationships/comments",
                        "related": "http://example.com/articles/1/comments"
                    },
                    "data": [
                        {
                            "type": "comments",
                            "id": "5"
                        },
                        {
                            "type": "comments",
                            "id": "12"
                        }
                    ]
                }
            },
            "links": {
                "self": "http://example.com/articles/1"
            }
        }
    ],
    "included": [
        {
            "type": "people",
            "id": "9",
            "attributes": {
                "firstName": "Dan",
                "lastName": "Gebhardt",
                "twitter": "dgeb"
            },
            "links": {
                "self": "http://example.com/people/9"
            }
        },
        {
            "type": "comments",
            "id": "5",
            "attributes": {
                "body": "First!"
            },
            "relationships": {
                "author": {
                    "data": {
                        "type": "people",
                        "id": "2"
                    }
                }
            },
            "links": {
                "self": "http://example.com/comments/5"
            }
        },
        {
            "type": "comments",
            "id": "12",
            "attributes": {
                "body": "I like XML better"
            },
            "relationships": {
                "author": {
                    "data": {
                        "type": "people",
                        "id": "9"
                    }
                }
            },
            "links": {
                "self": "http://example.com/comments/12"
            }
        }
    ]
}
"""
