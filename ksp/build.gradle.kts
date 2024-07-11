plugins {
  kotlin("jvm")
  id("com.eygraber.conventions-kotlin-library")
  id("com.eygraber.conventions-detekt")
}

dependencies {
  implementation(projects.jsonapi)
  implementation(projects.jsonapiTyped)

  implementation(libs.kotlinPoet.ksp)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.ksp)
}
