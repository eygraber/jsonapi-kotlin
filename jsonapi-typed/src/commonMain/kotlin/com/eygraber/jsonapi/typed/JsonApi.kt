package com.eygraber.jsonapi.typed

import com.eygraber.jsonapi.JsonApiDocument
import com.eygraber.jsonapi.JsonApiResourceLinkage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

public class JsonApi(json: Json) {
  public interface Resource {
    public val id: ResourceId
    public val lid: ResourceId get() = ResourceId.NoId
  }

  public val json: Json = Json(from = json) {
    ignoreUnknownKeys = true
  }

  public inline fun <reified T : Resource> decodeResource(data: JsonApiDocument.Resource): T? {
    val attributesJson = data.data.attributes?.toMutableMap() ?: mutableMapOf()

    data.data.id?.let { id ->
      attributesJson.put("id", JsonPrimitive(id))
    }

    data.data.lid?.let { lid ->
      attributesJson.put("lid", JsonPrimitive(lid))
    }

    // Merge relationships into attributes
    data.data.relationships?.forEach { (key, relationship) ->
      val relationshipAttributes = when(val relationshipData = relationship.data) {
        is JsonApiResourceLinkage.Many ->
          JsonArray(
            relationshipData.data.mapNotNull { resourceIdentifier ->
              val includedResourceAttributes = data.included?.find { includedResource ->
                includedResource.type == resourceIdentifier.type &&
                  (
                    (includedResource.id != null && includedResource.id == resourceIdentifier.id) ||
                      (includedResource.lid != null && includedResource.lid == resourceIdentifier.lid)
                    )
              }?.attributes?.toMutableMap()

              resourceIdentifier.id?.let { relatedResourceId ->
                includedResourceAttributes?.put("id", JsonPrimitive(relatedResourceId))
              }

              resourceIdentifier.lid?.let { relatedResourceLid ->
                includedResourceAttributes?.put("lid", JsonPrimitive(relatedResourceLid))
              }

              includedResourceAttributes?.let(::JsonObject)
            },
          )

        is JsonApiResourceLinkage.One ->
          relationshipData.data.let { resourceIdentifier ->
            val includedResourceAttributes = data.included?.find { includedResource ->
              includedResource.type == resourceIdentifier.type &&
                (
                  (includedResource.id != null && includedResource.id == resourceIdentifier.id) ||
                    (includedResource.lid != null && includedResource.lid == resourceIdentifier.lid)
                  )
            }?.attributes?.toMutableMap()

            resourceIdentifier.id?.let { relatedResourceId ->
              includedResourceAttributes?.put("id", JsonPrimitive(relatedResourceId))
            }

            resourceIdentifier.lid?.let { relatedResourceLid ->
              includedResourceAttributes?.put("lid", JsonPrimitive(relatedResourceLid))
            }

            includedResourceAttributes?.let(::JsonObject)
          }

        null -> JsonNull
      }

      relationshipAttributes?.let { attributesJson[key] = it }
    }

    // Decode the merged attributes into the desired type
    return json.decodeFromJsonElement(JsonObject(attributesJson))
  }

  public inline fun <reified T : Resource> decodeResources(data: JsonApiDocument.Resources): List<T> =
    data.data.map { resource ->
      val attributesJson = resource.attributes?.toMutableMap() ?: mutableMapOf()

      resource.id?.let { id ->
        attributesJson.put("id", JsonPrimitive(id))
      }

      resource.lid?.let { lid ->
        attributesJson.put("lid", JsonPrimitive(lid))
      }

      // Merge relationships into attributes
      resource.relationships?.forEach { (key, relationship) ->
        val relationshipAttributes = when(val relationshipData = relationship.data) {
          is JsonApiResourceLinkage.Many ->
            JsonArray(
              relationshipData.data.mapNotNull { resourceIdentifier ->
                val includedResource = data.included?.find { includedResource ->
                  includedResource.type == resourceIdentifier.type &&
                    (
                      (includedResource.id != null && includedResource.id == resourceIdentifier.id) ||
                        (includedResource.lid != null && includedResource.lid == resourceIdentifier.lid)
                      )
                }

                val includedResourceAttributes = includedResource?.attributes?.toMutableMap()

                resourceIdentifier.id?.let { relatedResourceId ->
                  includedResourceAttributes?.put("id", JsonPrimitive(relatedResourceId))
                }

                resourceIdentifier.lid?.let { relatedResourceLid ->
                  includedResourceAttributes?.put("lid", JsonPrimitive(relatedResourceLid))
                }

                includedResource?.links?.let { relatedResourceLinks ->
                  includedResourceAttributes?.put("links", json.encodeToJsonElement(relatedResourceLinks))
                }

                includedResourceAttributes?.let(::JsonObject)
              },
            )

          is JsonApiResourceLinkage.One ->
            relationshipData.data.let { resourceIdentifier ->
              val includedResource = data.included?.find { includedResource ->
                includedResource.type == resourceIdentifier.type &&
                  (
                    (includedResource.id != null && includedResource.id == resourceIdentifier.id) ||
                      (includedResource.lid != null && includedResource.lid == resourceIdentifier.lid)
                    )
              }

              val includedResourceAttributes = includedResource?.attributes?.toMutableMap()

              resourceIdentifier.id?.let { relatedResourceId ->
                includedResourceAttributes?.put("id", JsonPrimitive(relatedResourceId))
              }

              resourceIdentifier.lid?.let { relatedResourceLid ->
                includedResourceAttributes?.put("lid", JsonPrimitive(relatedResourceLid))
              }

              includedResource?.links?.let { relatedResourceLinks ->
                includedResourceAttributes?.put("links", json.encodeToJsonElement(relatedResourceLinks))
              }

              includedResourceAttributes?.let(::JsonObject)
            }

          null -> JsonNull
        }

        attributesJson[key] = relationshipAttributes ?: JsonNull
      }

      // Decode the merged attributes into the desired type
      json.decodeFromJsonElement<T>(JsonObject(attributesJson))
    }
}
