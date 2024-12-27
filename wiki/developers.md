<!-- TOC -->
* [Wiki](/wiki/wiki.md)
  * [Getting Started](/wiki/getting_started.md)
    * [Installation](/wiki/getting_started.md#installation)
  * [Server Owners](/wiki/server_owners.md)
    * [Resource Pack Management](/wiki/server_owners/resource_pack_management.md#resource-pack-management)
      * [Configuration](/wiki/server_owners/resource_pack_management.md#configuration)
      * [Commands](/wiki/server_owners/resource_pack_management.md#commands)
      * [GitHub Integration](/wiki/server_owners/resource_pack_management.md#github-integration)
  * [Developers](/wiki/developers.md)
    * [Implementation](/wiki/developers.md#implementing-quipt)
    * [Ephemeral Advancements](/wiki/developers/ephemeral_advancements.md)
    * [Message Utils](/wiki/developers/messages.md)
      * [Variable Replacements](/wiki/developers/messages.md#variable-replacements)
<!-- TOC -->

# Developers

Here you'll find information on how to implement and interact with QUIPT with your own plugins.

## Implementing QUIPT

First, you'll need to make sure that QUIPT is installed on your server. You can find more information on how to do this
in the [Getting Started](/wiki/getting_started.md) page. Once it's installed, you'll want to add it as a dependency in
your plugin.

You can check for the latest build for QUIPT on the
repository [here](https://repo.vanillaflux.com/service/rest/repository/browse/quipt/).

### Gradle

```gradle
repositories {
    maven {
        name = "quipt"
        url = "https://repo.vanillaflux.com/repository/quipt/"
    }
}
```

```gradle
dependencies {
    implementation("me.quickscythe:quipt:0.0.1-1.21-SNAPSHOT-B1")
}
```

### Paperweight
With paperweight, you can run the server with the plugin jars from the configuration for easy testing.

```gradle
runServer {
    pluginJars.from(configurations.runtimeClasspath)
}
```

### Maven

```maven
<repositories>
    <repository>
        <id>quipt</id>
        <url>https://repo.vanillaflux.com/repository/quipt/</url>
    </repository>
</repositories>
```

```maven
<dependency>
  <groupId>me.quickscythe</groupId>
  <artifactId>quipt</artifactId>
  <version>0.0.1-SNAPSHOT-B1</version>
</dependency>
```

### Paper
To use QUIPT with Paper, you'll need to shade the dependencies into your plugin jar. You can do this with the MavenLibraryResolver class.
```java
public class TestPluginLoader implements PluginLoader {

    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addDependency(new Dependency(new DefaultArtifact("me.quickscythe:quipt:0.0.1-1.21-SNAPSHOT-B1"), null));
        resolver.addRepository(new RemoteRepository.Builder("quipt", "default", "https://repo.vanillaflux.com/repository/quipt/").build());

        classpathBuilder.addLibrary(resolver);
    }
}
```
You can find more information on how to use the MavenLibraryResolver class in the [Paper Docs](https://docs.papermc.io/paper/dev/getting-started/paper-plugins#loaders).

