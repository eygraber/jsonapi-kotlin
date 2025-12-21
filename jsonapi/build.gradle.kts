plugins {
  id("com.android.lint")
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-kmp-library")
  id("com.eygraber.conventions-detekt2")
  id("com.eygraber.conventions-publish-maven-central")
  alias(libs.plugins.kotlinxSerialization)
}

kotlin {
  allKmpTargets(
    project = project,
    androidNamespace = "com.eygraber.jsonapi",
  )

  sourceSets {
    commonMain {
      dependencies {
        api(libs.kotlinx.serialization.json)
      }
    }

    commonTest {
      dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-annotations-common"))
      }
    }
  }
}
