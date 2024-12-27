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
    * [Config Manager](/wiki/developers/config.md)
      * [Creating a Config Template](/wiki/developers/config.md#creating-a-config-template)
      * [Registering a Config](/wiki/developers/config.md#registering-a-config)
      * [Using a Config](/wiki/developers/config.md#using-a-config)
<!-- TOC -->
# Config Manager
The Config Manager is a utility class that allows you to easily manage the configuration files for QUIPT. The Config Manager allows you to load, save, and access configuration files with ease.

## Creating a Config Template
Config Templates are classes that represent the structure of a configuration file. Config Templates are annotated with `@ConfigTemplate` and `@ConfigValue` annotations, as well as extends the `Config` class. The `@ConfigTemplate` annotation specifies the name and extension of the configuration file, while the `@ConfigValue` annotation specifies a field that should be serialized to the configuration file. While other extensions can be used, currently, the file contents will always be json. Below is an example of a Config Template

```java
@ConfigTemplate(name="config", ext="json")
public class DefaultConfig extends Config {
    
    @ConfigValue
    public String stringValue = "Hello, World!";
    
    @ConfigValue
    public int intValue = 42;
}
```

## Registering a Config
To register a configuration file, you can use the `ConfigManager#registerConfig` method. This method will create a new configuration file in the `plugins/<your_plugin>/` directory with the file name and extension specified in the `ConfigTemplate` annotation. Below is an example of how to register a configuration file

```java

public class MyPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        ConfigManager.registerConfig(this, DefaultConfig.class);
    }
}
```

## Using a Config
To access a configuration file, you can use the `ConfigManager#getConfig` method. This method will return an instance of the specified configuration file. Below is an example of how to access a configuration file

```java
//...
DefaultConfig config = ConfigManager.getConfig(plugin, DefaultConfig.class);
// When the file is loaded, the values will be set to the values in the file
config.stringValue = "Goodbye, World!";
// When the file is saved, the values set to the instance will be saved to the file
config.save();
//...
```