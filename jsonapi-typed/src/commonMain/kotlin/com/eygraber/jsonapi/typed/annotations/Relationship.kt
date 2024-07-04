package com.eygraber.jsonapi.typed.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY)
public annotation class Relationship(val value: String)
