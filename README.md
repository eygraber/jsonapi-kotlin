# JSON:API Kotlin

`jsonapi-kotlin` is a Kotlin Multiplatform library for working with [JSON:API](https://jsonapi.org/) documents.

It integrates with [kotlinx-serialization](https://github.com/Kotlin/kotlinx.serialization) to allow deserializing `String` and `JsonObject` representations of a JSON:API document into a Kotlin type, and for serializing the Kotlin type to a `String` or `JsonObject`.

You can use this library with all targets supported by [kotlinx-serialization](https://github.com/Kotlin/kotlinx.serialization).

## Gradle

```kotlin
repositories {
  mavenCentral()
}

dependencies {
  implementation("com.eygraber:jsonapi-kotlin:0.2.0")
  
  // kotlinx.serialization json is used for the actual serialization
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:<latest version>")
}
```

## Usage

With a `kotlinx.serialization.json.Json` instance, you can decode a `JsonApiDocument` from a `String` or `JsonObject`:

```kotlin
val json = Json()

val document = json.decodeFromString<JsonApiDocument>(string)

// OR

val document = json.decodeFromJsonElement<JsonApiDocument>(jsonObject)
```

This will return a generic `JsonApiDocument`. If you know the specific type of `JsonApiDocument` you are working with, you can specify it explicitly so that you don't need to cast:

```kotlin
val resource = json.decodeFromString<JsonApiDocument.Resource>(string)
val resources = json.decodeFromString<JsonApiDocument.Resources>(string)
val resourceIdentifier = json.decodeFromString<JsonApiDocument.Identifier>(string)
val resourceIdentifiers = json.decodeFromString<JsonApiDocument.Identifiers>(string)
val meta = json.decodeFromString<JsonApiDocument.Meta>(string)
val errors = json.decodeFromString<JsonApiDocument.Errors>(string)
```

You can also encode the `JsonApiDocument` to a `String` or `JsonObject`:

```kotlin
json.encodeToString(document)

// OR

json.encodeToJsonElement(document)
```

## In Progress

There is a KSP artifact being worked on that will make it easier to extract your domain types from a `JsonApiDocument`. 
