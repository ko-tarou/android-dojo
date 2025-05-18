plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  kotlin("kapt")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.github.mzkii.apps.android.dojo2025"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.github.mzkii.apps.android.dojo2025"
    minSdk = 29
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(libs.androidx.ui.test.junit4.android)
  val room_version = "2.6.1"

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.espresso.core)
  implementation(libs.androidx.animation.core.lint)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  //追加



  //Testのやつ
  androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.8")

  //謎
  implementation("androidx.compose.ui:ui:1.6.1")
  implementation("androidx.compose.animation:animation:1.6.1")


  //Hiltのやつ
  implementation("com.google.dagger:hilt-android:2.50")
  kapt("com.google.dagger:hilt-android-compiler:2.50")

  // Jetpack Compose 用の Hilt 拡張
  implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

  // Jetpack Compose 関連（必要なら追加）
  implementation("androidx.compose.runtime:runtime-livedata:1.6.1")
  implementation("androidx.compose.ui:ui-tooling-preview:1.6.1")
  implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

  // Kotlin Coroutines (非同期処理用)
  implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
  // 🔹 DataStore (Preferences)
  implementation ("androidx.datastore:datastore-preferences:1.0.0")

  //Roomのやつ
  implementation("androidx.room:room-runtime:$room_version")
  kapt("androidx.room:room-compiler:$room_version")
  implementation("androidx.room:room-ktx:$room_version")

  //Composeのやつ
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
  //Material3のやつ
  implementation("androidx.compose.material3:material3:1.3.1")

  //アイコンのやつ
  implementation("androidx.compose.material:material-icons-extended:1.5.0")
  //edge to edgeのやつ
  implementation("androidx.activity:activity-ktx:1.8.0")
  //画像のやつ
  implementation("io.coil-kt:coil-compose:2.4.0")
  //画面遷移のやつ
  implementation("androidx.navigation:navigation-compose:2.7.5")
  //画面遷移のアニメーションのやつ
  implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0")
}
