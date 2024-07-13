// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        val majorVersionCode = libs.versions.majorVersionCode.get().toInt()
        val minorVersionCode = libs.versions.minorVersionCode.get().toInt()
        val patchVersionCode = libs.versions.patchVersionCode.get().toInt()

        val appVersionCode = libs.versions.appVersionCode.get().toInt()
        val appVersionName = "$majorVersionCode.$minorVersionCode.$patchVersionCode"

        set("appVersionCode", appVersionCode)
        set("appVersionName", appVersionName)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.ksp) apply false
}