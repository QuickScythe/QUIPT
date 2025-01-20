rootProject.name = "quipt"

include(":core")

//include(":paper")
//include(":bot")

implementation("paper")
implementation("bot")

fun implementation(name: String) {
    val project = ":$name"
    include(project)
    project(project).projectDir = file("implementations/$name")
}
dependencyResolutionManagement {
    versionCatalogs {
        register("libs") {
            from(files("libs.versions.toml"))
        }
    }
}