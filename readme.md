# QUIPT
![Latest build status](https://ci.vanillaflux.com/job/block_bridge_api/badge/icon)

QUIPT is an ambitious project that aims to provide a handful of tools to both server owners and developers. Be sure to check out the [wiki](./wiki/wiki.md) for a more in-depth description of how to use this plugin.

## Server Owner Features
QUIPT was created with developers in mind, however also provides some features for server owners to use "out of the box"

### Resource Pack Management
QUIPT comes with an automatic resourcepack manager. Fill out the `plugins/QUIPT/resources.json` file with the GitHub repository information that contains your server resourcepack, and make sure to set `serverIp` to the IP your server is being hosted on. You can manually trigger a pack update by typing `/resourcepack update`, or you can set up GitHub to send a webhook to`<your-ip>:<resource-pack-port>/update/` to automatically update the pack every time the repository receives a new push.

## Developer Features
QUIPT was designed to give Paper plugin developers a simple-to-use and lightweight library focused on providing features that _should_ be easier to implement in the base API.

### EphemeralAdvancements
EphemeralAdvancements are QUIPT's way of sending custom toasts to the clients. Since paper doesn't have any method of sending custom toasts, we hook into the Bukkit advancement API and load the EphemeralAdvancements at the same time you create a new instance for it, then is deleted from the server once it's been sent to a player. This means once you send an EphemeralAdvancement to a player, you must kill the instance before you accidentally send it to another player.
