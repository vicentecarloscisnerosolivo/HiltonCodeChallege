import org.gradle.kotlin.dsl.support.kotlinCompilerOptions

plugins {
    kotlin("kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)

}

android {
    namespace = "com.vcco.hiltoncodechallenge"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vcco.hiltoncodechallenge"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BASE_URL", "\"${project.properties["base_url"]}\"")
        buildConfigField("String", "DATABASE_NAME", "\"${project.properties["database_name"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}


dependencies {
    //Android core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //ViewModel
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //GSON
    implementation(libs.gson)
    implementation(libs.gson.converter)

    //Room
    implementation(libs.room)
    kapt(libs.room.compiler)

    //Retrofit
    implementation(libs.retrofit)

    //OkHttp
    implementation(libs.okHttp)
    implementation(libs.okHttp.logging.interceptor)

    //Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compilation)

    //Testing
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Mockito
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.android)

    //Hilt Testing
    testImplementation(libs.dagger.hilt.testing)
    kaptTest(libs.dagger.hilt.compilation)
}

kapt{
    correctErrorTypes = true
}