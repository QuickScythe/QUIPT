API changes documented on the [wiki](https://github.com/QuickScythe/QUIPT/wiki/)

<!-- TOC -->
* [Developers](#developers)
  * [ItemUtils (wip)](#itemutils-wip)
  * [SessionManager](#sessionmanager)
  * [Heartbeats and Flutters](#heartbeats-and-flutters)
<!-- TOC -->
# Developers
Below is a list of new features Plugin Developers can take advantage of in QUIPT.
## ItemUtils (wip)
ItemUtils is a utility class that provides a number of useful methods for working with items. It is designed to be used in conjunction with the `ItemStack` class. This is a work in progress and is subject to change.

## SessionManager
SessionManager is a utility class that provides a number of useful methods for working with sessions. This provides a way to manage sessions and their associated data.

## Heartbeats and Flutters
The `Heartbeat` is the main loop QUIPT uses to run timed tasks, while `Flutters` are the tasks themselves. Flutters are functional interfaces that return a boolean value, if the value returns false, the task will be removed from the Heartbeat.

# Server Owners
Below is a list of new features Server Owners can take advantage of in QUIPT.

## Sessions
Sessions are a way to store data for a player that persists across server restarts. This is useful for storing player data that needs to be accessed by multiple plugins. As a server owner, there is no current way to customize this data (though this is expected to change in the future), but this does provide a dataset you can use externally.