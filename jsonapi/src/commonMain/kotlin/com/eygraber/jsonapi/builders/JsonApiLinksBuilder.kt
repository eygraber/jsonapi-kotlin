package com.eygraber.jsonapi.builders

import com.eygraber.jsonapi.JsonApiLink
import com.eygraber.jsonapi.JsonApiLinks

public class JsonApiLinksBuilder {
  private var selfLink: JsonApiLink? = null
  private var relatedLink: JsonApiLink? = null
  private var firstLink: JsonApiLink? = null
  private var lastLink: JsonApiLink? = null
  private var prevLink: JsonApiLink? = null
  private var nextLink: JsonApiLink? = null
  private val additionalLinks: MutableMap<String, JsonApiLink> = mutableMapOf()

  public var self: String
    get() = error("self is not meant to be referenced")
    set(value) {
      selfLink = JsonApiLink(value)
    }

  public fun self(self: String, serializeAsPrimitive: Boolean) {
    selfLink = JsonApiLink(self, shouldSerializeHrefAsPrimitive = serializeAsPrimitive)
  }

  public fun self(self: JsonApiLink) {
    selfLink = self
  }

  public var related: String
    get() = error("related is not meant to be referenced")
    set(value) {
      relatedLink = JsonApiLink(value)
    }

  public fun related(related: String, serializeAsPrimitive: Boolean) {
    relatedLink = JsonApiLink(related, shouldSerializeHrefAsPrimitive = serializeAsPrimitive)
  }

  public fun related(related: JsonApiLink) {
    relatedLink = related
  }

  public var first: String
    get() = error("first is not meant to be referenced")
    set(value) {
      firstLink = JsonApiLink(value)
    }

  public fun first(first: String, serializeAsPrimitive: Boolean) {
    firstLink = JsonApiLink(first, shouldSerializeHrefAsPrimitive = serializeAsPrimitive)
  }

  public fun first(first: JsonApiLink) {
    firstLink = first
  }

  public var last: String
    get() = error("last is not meant to be referenced")
    set(value) {
      lastLink = JsonApiLink(value)
    }

  public fun last(last: String, serializeAsPrimitive: Boolean) {
    lastLink = JsonApiLink(last, shouldSerializeHrefAsPrimitive = serializeAsPrimitive)
  }

  public fun last(last: JsonApiLink) {
    lastLink = last
  }

  public var prev: String
    get() = error("prev is not meant to be referenced")
    set(value) {
      prevLink = JsonApiLink(value)
    }

  public fun prev(prev: String, serializeAsPrimitive: Boolean) {
    prevLink = JsonApiLink(prev, shouldSerializeHrefAsPrimitive = serializeAsPrimitive)
  }

  public fun prev(prev: JsonApiLink) {
    prevLink = prev
  }

  public var next: String
    get() = error("next is not meant to be referenced")
    set(value) {
      nextLink = JsonApiLink(value)
    }

  public fun next(next: String, serializeAsPrimitive: Boolean) {
    nextLink = JsonApiLink(next, shouldSerializeHrefAsPrimitive = serializeAsPrimitive)
  }

  public fun next(next: JsonApiLink) {
    nextLink = next
  }

  public fun custom(name: String, custom: JsonApiLink) {
    additionalLinks[name] = custom
  }

  public fun custom(name: String, custom: String) {
    additionalLinks[name] = JsonApiLink(custom)
  }

  public fun build(): JsonApiLinks = JsonApiLinks(
    self = selfLink,
    related = relatedLink,
    first = firstLink,
    last = lastLink,
    prev = prevLink,
    next = nextLink,
    additionalLinks = additionalLinks,
  )
}
