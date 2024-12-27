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
# Messages
QUIPT provides a way to store messages in a `messages.json`, either for repeated usage, or to allow server owners to configure messages. Messages can be managed by the `MessageUtils` class. You'll want to add your messages when your plugin enables. Once a messages has been added, it doesn't need to be added again, but it also doesn't override the message if it already exists, so adding all your messages in the `onEnable` method is a good idea to make sure all messages are added.
```java
import me.quickscythe.quipt.utils.chat.MessageUtils;

public class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        MessageUtils.addMessage("test.msg.key", "This is a test message!");
    }
}

```

## Variable Replacements
Messages can contain variables that are replaced with values when the message is sent. Variables are denoted by `[#]` where `#` is an integer telling the `MessageUtils` class which variable to replace. Variables are replaced by the order of `#`, so `[0]` will be the first to be replaced, and `[1]` will be the second, and so on. Variables are supplied when the message is requested, and are replaced in the order they are supplied. If a variable is missing, it will be left as `[#]`. If a variable is supplied that isn't in the message, it will be ignored. Here's an example of a message with variables:
```java
import me.quickscythe.quipt.utils.chat.MessageUtils;
//...
public void onEnable(){
    MessageUtils.addMessage("test.msg.key", "Hello, [0]! You have [1] messages!");
    doSomething("QuickScythe", 5);
}
//...

//...
public void doSomething(String playerName, int messageCount){
    Component message = MessageUtils.getMessage("test.msg.key", playerName, messageCount);
    Bukkit.broadcast(message);
}
```
Here, the output of `doSomething` would be
```output
Hello, QuickScythe! You have 5 messages!
```