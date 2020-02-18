tasks {
    register("delete", Delete::class.java) {
        delete(buildDir)
    }
}