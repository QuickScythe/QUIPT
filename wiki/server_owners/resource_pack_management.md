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
## Resource Pack Management
QUIPT allows you to manage resource packs for your server. First you'll need to configure the resource pack settings in the `resources.json` file. You can find more information on how to do this in the [Configuration](/wiki/server_owners/resource_pack_management.md#configuration) section. Once you've configured the resource pack settings, you can use the `/resourcepack` command to manage resource packs on your server (see [Commands](/wiki/server_owners/resource_pack_management.md#commands) for more information), or you can set up GitHub to automatically update the resource pack for you (see [GitHub Integration](/wiki/server_owners/resource_pack_management.md#github-integration) for more information).

### Configuration
Below is the default configuration for the resource pack settings in the `resources.json` file. You can change these settings to fit your needs.
```json5
{
  "server_ip": "127.0.0.1",
  "server_port": 9009,
  "repo_url": "",
  "repo_branch": "main",
  "is_private": false,
  "repo_username": "user",
  "repo_password": "pass",
  "version": 1
}
```
- `server_ip`: The IP address of your server.
- `server_port`: The port you want your resource pack to be served on. Currently, must be a port that is not already in use, however it is planned to allow for use of the Minecraft server port in the future.
- `repo_url`: The URL of the GitHub repository that contains your resource pack. Does not contain the `.git` extension.
- `repo_branch`: The branch of the repository that contains your resource pack. `main` is the default branch name for GitHub repositories.
- `is_private`: Whether the repository is private. If the repository is private, you will need to provide a username and password.
- `repo_username`: The username of the GitHub account that has access to the repository. Only required if `is_private` is set to `true`.
- `repo_password`: The password of the GitHub account that has access to the repository. Only required if `is_private` is set to `true`.
- `version`: This is a value used to determine the Config version. This should not be changed.

### Commands
There is currently a limited number of commands available for managing resource packs. Currently, there is just one major command for updating the pack manually (`/resourcepack update`), and one for reloading the pack (`/resourcepack reload`). Reloading the resourcepack works similar to pressing F3 + T in game, it will reload the resource pack without needing to disconnect from the server.

### GitHub Integration
You can set up GitHub to automatically update the resource pack for you. To do this, you'll need to configure the resource pack settings in the `resources.json` file (see [Configuration](#configuration) for more information). Once you've configured the resource pack settings, you can set up a GitHub webhook to send a POST request to the `/update` endpoint on your server (`http://{server_ip}:{server_port}/update`). This will automatically update the resource pack on your server whenever you push changes to the repository. You can find more information on how to set up a GitHub webhook [here](https://docs.github.com/en/developers/webhooks-and-events/webhooks/creating-webhooks).