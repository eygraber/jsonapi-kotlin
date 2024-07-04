package com.eygraber.json.api.sample

import com.eygraber.jsonapi.typed.JsonApi
import com.eygraber.jsonapi.typed.ResourceId
import com.eygraber.jsonapi.typed.annotations.Relationship
import com.eygraber.jsonapi.typed.annotations.Type
import kotlinx.serialization.Serializable

@Serializable
@Type("foo")
public data class Foo(
  override val id: ResourceId = ResourceId("Test1"),
  val name: String,
  @Relationship("bar") val bar: Bar,
) : JsonApi.Resource

@Serializable
@Type("bar")
public data class Bar(
  override val id: ResourceId = ResourceId("Test2"),
  @Relationship("baz") val baz: Baz,
  @Relationship("boom") val boom: Boom,
) : JsonApi.Resource

@Serializable
@Type("baz")
public data class Baz(
  override val id: ResourceId = ResourceId("Test3"),
  override val lid: ResourceId = ResourceId("Test3Local"),
  val boo: String,
) : JsonApi.Resource

@Serializable
@Type("boom")
public data class Boom(
  override val id: ResourceId = ResourceId.NoId,
  override val lid: ResourceId = ResourceId("Test4"),
  val boom: String,
) : JsonApi.Resource
