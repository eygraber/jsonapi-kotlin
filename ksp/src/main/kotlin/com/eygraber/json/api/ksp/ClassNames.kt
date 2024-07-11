package com.eygraber.json.api.ksp

import com.eygraber.jsonapi.JsonApiDocument
import com.eygraber.jsonapi.JsonApiLinks
import com.eygraber.jsonapi.JsonApiObject
import com.eygraber.jsonapi.JsonApiRelationship
import com.eygraber.jsonapi.JsonApiResource
import com.eygraber.jsonapi.JsonApiResourceIdentifier
import com.eygraber.jsonapi.JsonApiResourceLinkage
import com.eygraber.jsonapi.typed.JsonApi
import com.eygraber.jsonapi.typed.JsonApiDocumentBuilder
import com.eygraber.jsonapi.typed.JsonApiIncludedResources
import com.squareup.kotlinpoet.asClassName
import kotlinx.serialization.json.JsonObject

internal object ClassNames {
  val jsonApi = JsonApi::class.asClassName()
  val jsonApiDocumentDataResources = JsonApiDocument.Resources::class.asClassName()
  val jsonApiDocumentDataResource = JsonApiDocument.Resource::class.asClassName()
  val jsonApiLinks = JsonApiLinks::class.asClassName()
  val jsonApiObject = JsonApiObject::class.asClassName()
  val jsonApiRelationship = JsonApiRelationship::class.asClassName()
  val jsonApiResource = JsonApiResource::class.asClassName()
  val jsonApiResourceIdentifier = JsonApiResourceIdentifier::class.asClassName()
  val jsonApiResourceLinkageOne = JsonApiResourceLinkage.One::class.asClassName()
  val jsonApiResourceLinkageMany = JsonApiResourceLinkage.Many::class.asClassName()
  val jsonObject = JsonObject::class.asClassName()

  val includedResources = JsonApiIncludedResources::class.asClassName()
  val jsonApiDocumentBuilders = JsonApiDocumentBuilder::class.asClassName()
  val jsonApiDocumentBuildersId = JsonApiDocumentBuilder.Id::class.asClassName()
}
