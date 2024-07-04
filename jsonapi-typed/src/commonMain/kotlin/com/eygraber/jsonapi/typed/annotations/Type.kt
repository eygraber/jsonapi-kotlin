package com.eygraber.jsonapi.typed.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
public annotation class Type(val value: String)
