package com.eygraber.json.api.ksp

import com.eygraber.json.api.JsonApi
import com.eygraber.json.api.JsonApiDocumentBuilder
import com.eygraber.json.api.JsonApiIncludedResources
import com.eygraber.json.api.spec.JsonApiDocument
import com.eygraber.json.api.spec.JsonApiObject
import com.eygraber.json.api.spec.JsonApiRelationship
import com.eygraber.json.api.spec.JsonApiResource
import com.eygraber.json.api.spec.JsonApiResourceIdentifier
import com.squareup.kotlinpoet.asClassName
import kotlinx.serialization.json.JsonObject

internal object ClassNames {
  val jsonApi = JsonApi::class.asClassName()
  val jsonApiDocumentDataResources = JsonApiDocument.Resources::class.asClassName()
  val jsonApiDocumentDataLinks = JsonApiDocument.Data.Links::class.asClassName()
  val jsonApiDocumentDataResource = JsonApiDocument.Resource::class.asClassName()
  val jsonApiObject = JsonApiObject::class.asClassName()
  val jsonApiRelationship = JsonApiRelationship::class.asClassName()
  val jsonApiResource = JsonApiResource::class.asClassName()
  val jsonApiResourceIdentifier = JsonApiResourceIdentifier::class.asClassName()

  val jsonObject = JsonObject::class.asClassName()

  val includedResources = JsonApiIncludedResources::class.asClassName()
  val jsonApiDocumentBuilders = JsonApiDocumentBuilder::class.asClassName()
  val jsonApiDocumentBuildersId = JsonApiDocumentBuilder.Id::class.asClassName()
}
