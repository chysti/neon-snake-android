plugins { id("com.android.application") }

android {
    namespace = "com.stakan.neonsnake"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.stakan.neonsnake"
        minSdk = 23
        targetSdk = 35
        versionCode = 3
        versionName = "1.0.2"
    }
    signingConfigs {
        create("releaseUpload") {
            storeFile = rootProject.file("neon-snake-upload.jks")
            storePassword = rootProject.file("upload-key-password.txt").readText().trim()
            keyAlias = "neon-snake-upload"
            keyPassword = storePassword
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("releaseUpload")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies { implementation("com.google.android.gms:play-services-ads:23.6.0") }
