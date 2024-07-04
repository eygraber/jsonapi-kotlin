package com.eygraber.json.api.ksp

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName

internal fun FileSpec.Builder.addFunctionBuildDocumentFrom(
  typeInfo: TypeInfo,
  resourceBuilderFunctionName: String,
) {
  val resourceBuilderMN = MemberName(
    TypeProcessor.GEN_PACKAGE,
    resourceBuilderFunctionName,
    isExtension = true,
  )

  val resourceParameterName = typeInfo.type.simpleName.asString().replaceFirstChar(Char::lowercaseChar)

  addFunction(
    FunSpec.builder("buildDocumentFrom").apply {
      receiver(ClassNames.jsonApi)

      addParameter(
        resourceParameterName,
        typeInfo.type.toClassName(),
      )

      addParameter(
        ParameterSpec.builder(
          "meta",
          ClassNames.jsonObject.copy(nullable = true),
        ).defaultValue("null").build(),
      )

      addParameter(
        ParameterSpec.builder(
          "jsonapi",
          ClassNames.jsonApiObject.copy(nullable = true),
        ).defaultValue("null").build(),
      )

      addParameter(
        ParameterSpec.builder(
          "links",
          ClassNames.jsonApiLinks.copy(nullable = true),
        ).defaultValue("null").build(),
      )

      returns(ClassNames.jsonApiDocumentDataResource)

      addStatement(
        "val resourceAttributes = json.%M($resourceParameterName).%M\n",
        MemberNames.encodeToJsonElement,
        MemberNames.jsonObject,
      )

      addStatement(
        "val includedResources = %T()\n",
        ClassNames.includedResources,
      )

      addCode(
        """
        |val resource = %T.%M(
        |  attributes = resourceAttributes,
        |  isAddToIncludedResourcesNeeded = includedResources::isAddToIncludedResourcesNeeded,
        |  addToIncludedResources = includedResources::add,
        |)
        |
        |
        """.trimMargin(),
        ClassNames.jsonApiDocumentBuilders,
        resourceBuilderMN,
      )

      addCode(
        CodeBlock.of(
          """
          |return %T(
          |  data = resource,
          |  meta = meta,
          |  jsonapi = jsonapi,
          |  links = links,
          |  included = includedResources.includedResources,
          |)
          """.trimMargin(),
          ClassNames.jsonApiDocumentDataResource,
        ),
      )
    }.build(),
  )

  addFunction(
    FunSpec.builder("buildDocumentFrom").apply {
      receiver(ClassNames.jsonApi)

      addParameter(
        "${resourceParameterName}s",
        LIST.parameterizedBy(typeInfo.type.toClassName()),
      )

      addParameter(
        ParameterSpec.builder(
          "meta",
          ClassNames.jsonObject.copy(nullable = true),
        ).defaultValue("null").build(),
      )

      addParameter(
        ParameterSpec.builder(
          "jsonapi",
          ClassNames.jsonApiObject.copy(nullable = true),
        ).defaultValue("null").build(),
      )

      addParameter(
        ParameterSpec.builder(
          "links",
          ClassNames.jsonApiLinks.copy(nullable = true),
        ).defaultValue("null").build(),
      )

      returns(ClassNames.jsonApiDocumentDataResources)

      addStatement(
        "val resourceAttributes = json.%M(${resourceParameterName}s).%M\n",
        MemberNames.encodeToJsonElement,
        MemberNames.jsonArray,
      )

      addStatement(
        "val includedResources = %T()\n",
        ClassNames.includedResources,
      )

      addCode(
        """
        |val resources = resourceAttributes.map { resourceAttribute ->
        |  %T.%M(
        |    attributes = resourceAttribute.%M,
        |    isAddToIncludedResourcesNeeded = includedResources::isAddToIncludedResourcesNeeded,
        |    addToIncludedResources = includedResources::add,
        |  )
        |}
        |
        |
        """.trimMargin(),
        ClassNames.jsonApiDocumentBuilders,
        resourceBuilderMN,
        MemberNames.jsonObject,
      )

      addCode(
        CodeBlock.of(
          """
          |return %T(
          |  data = resources,
          |  meta = meta,
          |  jsonapi = jsonapi,
          |  links = links,
          |  included = includedResources.includedResources,
          |)
          """.trimMargin(),
          ClassNames.jsonApiDocumentDataResources,
        ),
      )
    }.build(),
  )
}
