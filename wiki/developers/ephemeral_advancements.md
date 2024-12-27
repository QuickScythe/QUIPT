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
## Ephemeral Advancements
Ephemeral Advancements are QUIPT's answer to sending custom toasts to players, without having it tied to an advancement in the player's advancement tree. Once the player has seen the toast, it will be removed from the server, so if you want to display the toast to multiple players, you'll need to create a new instance of `EphemeralAdvancement` for each player. The recommended way of creating a new `EphemeralAdvancement` is to use the `EphemeralAdvancement.Builder` class.

```java
import javax.swing.text.PlainDocument;

//...
EphemeralAdvancement.Builder builder = new EphemeralAdvancement.Builder()
        .title(text("Custom Toast!"))
        .icon(Material.DIAMOND)
        .frame(AdvancementDisplay.Frame.TASK);
for(Player player : Bukkit.getOnlinePlayers()){
    EphemeralAdvancement advancement = builder.build();
    advancement.send(player);
}
//...
```

