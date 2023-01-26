object Dependencies {
    const val core = "androidx.core:core-ktx:${Versions.coreVersion}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompatVersion}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
    const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragmentKtxVersion}"

    const val junit = "androidx.test.ext:junit:${Versions.androidJunitVersion}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"

    const val lifecycleRuntime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    const val lifecycleViewmodel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"

    object Versions {
        const val coroutinesVersion = "1.6.4"
        const val navigationVersion = "2.5.3"
        const val coreVersion = "1.9.0"
        const val appCompatVersion = "1.6.0"
        const val fragmentKtxVersion = "1.5.0"
        const val materialVersion = "1.6.1"
        const val constraintLayoutVersion = "2.1.4"
        const val junitVersion = "4.13.2"
        const val androidJunitVersion = "1.1.3"
        const val espressoVersion = "3.4.0"
        const val lifecycleVersion = "2.5.1"
        const val roomVersion = "2.5.0"
        const val daggerVersion = "2.41"
        const val work_version = "2.7.1"
    }
}
