# Idée:
- Ajouter un broadcast de message centré avec le support des \n pour ajouter plusieurs lignes
- Ajouter une option pour désactiver la tabulation des joueurs hors ligne
- Ajouter un placeholder pour transformet les caractères en lettre spécial

# Unreleased

- Added [BlockTracker](https://modrinth.com/plugin/blocktracker) for Player WorldEdit.
- Added a cache system for updating certain data in batches. This greatly reduces the number of SQL queries executed..
- Added a list of UUID blacklist from your server. It will no longer be able to connect.
- Added newline support for scoreboard lines with ``\n``.
- Fixed command `/vault give`, using the correct value for the player name.

# 1.0.1.8

- Added a cache for the nicknames of offline players.
- Added an option to disable the list of offline player usernames in the completion tab for certain commands.
- Added `vault-slot-type`, allowing you to define how vault slots are counted.
- Added `teleport-at-spawn-on-join`, enabling player teleportation to spawn upon joining.
- Fixed placeholders in messages.
- Fixed the `/skull` command and added support for hexadecimal format.
- Fixed default vault slot assignment by permission.
- Fixed duplicate keys in power tools.
- Fixed the SQL table for player slots.
- Fixed the button to reset vault names.
- Fixed title messages.


# 1.0.1.7

- Added a bot discord. Se bot allows linking your account discord to your minecraft account. Download the bot here: https://groupez.dev/resources/zessentials-discord-bot.340
- Added command ``/link <code>``, allows linking your minecraft account
- Added command ``/unlink``, allows unlinking your minecraft account
- Added placeholder ``%zessentials_user_has_discord_linked%``
- Fixed locations that could not be loaded if the world loaded after zEssentials
- You can use placeholders in the join and quit message

# 1.0.1.6

- You are required to use java 21
- Added ``%zessentials_can_repair_all%`` placeholder, indicates whether the player can fix everything
- Added ``%zessentials_count_repair_all%`` placeholder, counting the items to be repaired
- Added ``/repairall [<player>]``
- Added ``/tpahere <player>`` [#103](https://github.com/Maxlego08/zEssentials/issues/103)
- Fixed commands that could not be used from the console
- Fixed docs files
- Fixed warps inventory
- `zessentials_iteminhand_amount%` Returns the amount of items in the main hand
- `zessentials_iteminhand_custommodeldata%` Returns the custom model data of the item in hand
- `zessentials_iteminhand_displayname%` Returns the display name of the item in hand
- `zessentials_iteminhand_durability%` Returns the amount of durability left of the item in hand
- `zessentials_iteminhand_enchantmentlevel_%` Returns the level of a specific enchantment on the item in hand
- `zessentials_iteminhand_enchantments%` Returns the enchantments of the item in hand with their level
- `zessentials_iteminhand_fire_resistant%` Returns true if the item in hand is fire resistant
- `zessentials_iteminhand_glint%` Returns true if the item in hand has the glint enchantment
- `zessentials_iteminhand_hasenchantment_%` Returns true if the item in hand has at least one enchantment
- `zessentials_iteminhand_hasitemflag_%` Returns true if the item in hand has a specific itemflag
- `zessentials_iteminhand_hide_tooltip%` Returns true if the item in hand has its tooltip hidden
- `zessentials_iteminhand_hide_unbreakable%` Returns true if the tooltip unbreakable of the item in hand is hidden
- `zessentials_iteminhand_itemflags%` Returns the itemflags of the item in hand
- `zessentials_iteminhand_lore%` Returns the lore of the item in hand
- `zessentials_iteminhand_maxdurability%` Returns the maximum durability of the item in hand
- `zessentials_iteminhand_maxstacksize%` Returns the max stack size of the item in hand
- `zessentials_iteminhand_rarity%` Returns the rarity of the item in hand
- `zessentials_iteminhand_realname%` Returns the formatted material name of the item in hand
- `zessentials_iteminhand_repaircost%` Returns the repair cost of the item in hand
- `zessentials_iteminhand_type%` Returns the material name of the item in hand
- `zessentials_iteminhand_unbreakable%` Returns true if the item in hand is unbreakable
- `%zessentials_user_world%` Returns the name of the world the player is currently in
- `%zessentials_user_x%` Returns the x coordinate of the player
- `%zessentials_user_y%` Returns the y coordinate of the player
- `%zessentials_user_z%` Returns the z coordinate of the player
- `%zessentials_user_biome%` Returns the biome of the player
- `%zessentials_user_block_x%` Returns the block x coordinate of the player
- `%zessentials_user_block_y%` Returns the block y coordinate of the player
- `%zessentials_user_block_z%` Returns the block z coordinate of the player
- `%zessentials_server_name%` Returns the server name
- `%zessentials_server_uptime%` Returns the server update in format day, hour, minutes and seconds
- `%zessentials_server_uptime_in_second%` Returns the server update in second
- `%zessentials_last_random_number_<player name>%` Returns the last random number generated for the player within the
  last hour
- `%zessentials_last_random_player%` Returns the last random player name online
- `%zessentials_random_number_<from>_<to>%` Returns a random number between the two given arguments
- `%zessentials_random_player%` Returns a random player name online

# 1.0.1.5

- Added ``/clearinventory [<player>]`` [#101](https://github.com/Maxlego08/zEssentials/issues/124)
- Improve economy module with offline players
- Fixed teleport command with relative coordinates [#142](https://github.com/Maxlego08/zEssentials/issues/142)
- Added a method in the API to retrieve player’s transaction history
- Added a reason for each transaction made by the player
- Fixed sql port [#144](https://github.com/Maxlego08/zEssentials/issues/144)
- Fixed rtp with folia [#138](https://github.com/Maxlego08/zEssentials/issues/138)
- Fixed kit permission
- Fixed method ``stringToDuration`` [#143](https://github.com/Maxlego08/zEssentials/pull/143)
- Fixed the cooldown command for commands that don’t come from
  zEssentials [#137](https://github.com/Maxlego08/zEssentials/pull/137)
- Fixed teleport request [#134](https://github.com/Maxlego08/zEssentials/pull/134)
- Added ``/suicide`` [#135](https://github.com/Maxlego08/zEssentials/pull/135)
- Move ``commands.md``, `placeholders.md` and `permissions.md` in docs folder
- Added ``/eco give <economy> <player> <min amount> <max amount>``
  command [#120](https://github.com/Maxlego08/zEssentials/pull/120)
- Fixed teleport command if player doesn't exit [#112](https://github.com/Maxlego08/zEssentials/pull/112)

# 1.0.1.4

- Fixed auto update task for hologram module
- Fixed autocompletion for cooldown commands
- Fixed the cooldown system that could be applied to commands even if an error occurred
- Fixed folia on player join [#124](https://github.com/Maxlego08/zEssentials/issues/124)
- Debug player first joins at spawn location [#125](https://github.com/Maxlego08/zEssentials/issues/125)

# 1.0.1.3

- Add global commands for VoteParty [#115](https://github.com/Maxlego08/zEssentials/issues/115)
- Changing the commands of the vote party by zMenu actions, **you must update your configuration**.
- Fixed the appearance of holograms in other worlds
- Added command ``/fly get <player>``
- Added command ``/fly info``
- Added command ``/ess delete-world <world>``, allows you to delete data related to a world
- Fixed delete home sql request [#119](https://github.com/Maxlego08/zEssentials/issues/119)
- Added permission ``essentials.fly.safelogin``, Players with this permission will automatically enter fly mode upon
  logging in if they are suspended in the air. [#117](https://github.com/Maxlego08/zEssentials/issues/117)
- If the player does not have the `essentials.speed` permission, the walk and fly speed will be reset to default values
- Tab completion for editing hologram and itemrename [#116](https://github.com/Maxlego08/zEssentials/issues/116)

# 1.0.1.2

- Added a temporary fly with the `/fly` command
- Added permission `essentials.fly.unlimited`, allows fly without time restriction
- Added permission `essentials.fly.other`, allows to activate the fly to another player
- Added command ``/fly add <player> <seconds>``
- Added command ``/fly remove <player> <seconds>``
- Added command ``/fly set <player> <seconds>``
- Fixed hologram despawning [#99](https://github.com/Maxlego08/zEssentials/issues/99)
- Fixed holograms are in every world [#100](https://github.com/Maxlego08/zEssentials/issues/100)
- Added auto update for holograms
- Added placeholder ``%zessentials_user_fly_seconds%``, returns the number of seconds for temporary fly
- Added default money when player join [#105](https://github.com/Maxlego08/zEssentials/issues/105)
- Fixed seen command [#102](https://github.com/Maxlego08/zEssentials/issues/102)
- Fixed teleportation delay glitch [#96](https://github.com/Maxlego08/zEssentials/issues/96)
- Fixed vault register when economy is disable [#95](https://github.com/Maxlego08/zEssentials/issues/95)
- Added disable fly in certain worlds [#91](https://github.com/Maxlego08/zEssentials/issues/91)

# 1.0.1.1

- Added ``/spawn <player>`` (Permission: `essentials.spawn.other`)
- Added checking if player is vanished for various commands
- Fixed message when the player leave server
- Added permission ``essentials.back.death``, the player must have this permission to return to the place of his death
- Fixed teleport task when player is offline [#92](https://github.com/Maxlego08/zEssentials/issues/92)
- Fixed command ``/heal <player>`` and `/feed <player>` if you use it in the
  console [#90](https://github.com/Maxlego08/zEssentials/issues/90)
- Added two types of home usage MAX and STACK [#84](https://github.com/Maxlego08/zEssentials/issues/84)
- Fixed home delete inventory [#89](https://github.com/Maxlego08/zEssentials/issues/89)
- Fixed item display when you try to display an empty item
- Fixed tabulation with no argument needed
- Fixed message when you create a home
- Fixed economy give all command from console [#79](https://github.com/Maxlego08/zEssentials/issues/79)
- Fixed error when you die in another world [#76](https://github.com/Maxlego08/zEssentials/issues/76)

# 1.0.1.0

- Fixed command cooldown if permission was not set
- Fixed the system of economy that did not work with offline players

# 1.0.0.9

- Added the ability to change default vault names
- Added NMS support for 1.21.1
- Fixed an SQL query for updating homes with SQLITE
- Fixed an SQL query for updating cooldown with SQLITE [#74](https://github.com/Maxlego08/zEssentials/issues/74)
- Fixed creating homes that executed an SQL query for no reason
- Added command ``/freeze <player>``
- Added a command to confirm the overwrite of an already existing home. You can disable this option in
  `modules/homes/config.yml`
- Added a command to confirm the deletion of an home. You can disable this option in `modules/homes/config.yml`
- Fixed commands ``/sethome <player>:<home name>`` and ``/delhome <player>:<home name>`` which did not work if the
  player was online.
- Fixed command ``/wtp`` with folia

# 1.0.0.8

- Added error exception when you try to load a home if the world doesn't
  exist [#67](https://github.com/Maxlego08/zEssentials/issues/67)
- Implementation of the method ``boolean hasMoney(OfflinePlayer player, Economy economy, BigDecimal amount)`` and
  `BigDecimal getBalance(OfflinePlayer player, Economy economy)` in
  `EconomyModule` [#66](https://github.com/Maxlego08/zEssentials/issues/66)
- Fix sarah migration
- Fixed invsee command [#72](https://github.com/Maxlego08/zEssentials/issues/72)

# 1.0.0.7

- Updated the command `/endersee` to be compatible with offline players, added permission `essentials.endersee.offline`
- Updated the command `/invsee` to be compatible with offline players, added permission `essentials.invsee.offline`
- Fixed error with loading data [#59](https://github.com/Maxlego08/zEssentials/issues/59)
- Fixed night vision [#56](https://github.com/Maxlego08/zEssentials/issues/56)
- Changed aliases for PlayerWorldEdit from `pw` to `pwe` [#58](https://github.com/Maxlego08/zEssentials/issues/58)

# 1.0.0.6

- Added the command ``/showitem <code>``, Allows you to see the item that the player has in his hand. This command is
  used with the chat placeholder `[item]` [#43](https://github.com/Maxlego08/zEssentials/issues/43)
- Added the command ``/money <player>``, Shows the money of other players.
- Fixed messages [#39](https://github.com/Maxlego08/zEssentials/issues/39)
- Fixed default configuration for economy [#38](https://github.com/Maxlego08/zEssentials/issues/38)
- Fixed bug with ``/tp`` command [#37](https://github.com/Maxlego08/zEssentials/issues/37)
- Fixed nightvision messages
- Add the feature to manage vaults slots with permissions

```yaml
vault-permissions:
  - permission: zessentials.vault.size.player
    slots: 45
  - permission: zessentials.vault.size.vip
    slots: 90
  - permission: zessentials.vault.size.admin
    slots: 500
```

- Fixed title message placeholders
- Fixed somes messages [#47](https://github.com/Maxlego08/zEssentials/issues/47)

# 1.0.0.5

- Added the command `/ess convert HuskHomes`, allows converting the database from HuskHomes to zEssentials.
- Added the command `/ess convert AxVaults`, allows converting the database from AxVaults to zEssentials.
- Added the command `/mailbox give <player> <item> [<amount>]`, Add an item to a player’s mailbox.
- Added the command `/mailbox giveall <player> <item> [<amount>]`, Add an item to online player’s mailbox.
- Added the command `/vault give <player> <item> [<amount>]`, Add an item to player’s vault.
- Fixed CMI convert with invalid location for homes
- Fixed the bug that allowed adding items in the mailbox even if the module is disabled.
- Fixed command /rules who are not using the correct module.
- Added the module `WorldEdit`, module allows players to have access to a **player worldedit**. They will be able to use
  the commands like `/pw set`, `/pw cut` to place or break blocks. Each block placed must be paid, by default 5$ per
  blocks. You can configure the item worldedit, the number of blocks that the player can change at the same time and
  many other things, more information [here](https://zessentials.groupez.dev/modules/worldedit).

# 1.0.0.4

- Added the command `/ess convert PlauerVaultX`, allows converting the database from PlayerVaultX to zEssentials.
- Added the command `/ess convert Sunlight`, allows converting the database from Sunlight to zEssentials.
- Added the command `/ess convert CoinsEngine`, allows converting the database from CoinsEngine to zEssentials.
- Items in player vault will be displayed as a single item.
- Fixed night vision messages
- Fixed duplicate lines [#33](https://github.com/Maxlego08/zEssentials/issues/33)
- Upgrade to [Sarah](https://github.com/Maxlego08/Sarah/) version 1.10
- Fixed issue where savings by default could not be removed [#34](https://github.com/Maxlego08/zEssentials/issues/34).
  You must update your configuration like this
  Before

````yaml
economies:
  money:
    display-name: Money
````

After

````yaml
economies:
  - name: money
    display-name: Money
````

- Fixed permissions for night vision and vault commands
- Added command ``/sudo`` [#36](https://github.com/Maxlego08/zEssentials/issues/36)

# 1.0.0.3

- Change ``AsyncPlayerPreLoginEvent`` to ``PlayerLoginEvent``
- Change /tp command for adding coordinate. You have now ``/tp <x> <y> <z> <yaw> <pitch>`` and
  ``/tp <player> <x> <y> <z> <yaw> <pitch>``
- Fixed various messages in multiple languages
- Fixed text hologram default text [#25](https://github.com/Maxlego08/zEssentials/issues/25)
- Added command ``/nightvision`` (`/nv`), Provides a night vision effect

# 1.0.0.2

- Added the command `/ess convert EssentialsX`, allows converting the database from EssentialsX to zEssentials.
  Documentation: https://zessentials.groupez.dev/getting-started/convert#essentialsx
- Fixed the scoreboard title not appearing
- Added `/homes` alias for `/home` command for default configurations
- Added `/enchant` command
- Added enchantments list with aliases
- Added dutch translation [#19](https://github.com/Maxlego08/zEssentials/pull/19)

# 1.0.0.1

- Added the command `/ess convert CMI`, allows converting the database from CMI to zEssentials.
  Documentation: https://zessentials.groupez.dev/getting-started/convert#cmi