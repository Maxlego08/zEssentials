# Unreleased

- Added the command `/ess convert HuskHomes`, allows converting the database from HuskHomes to zEssentials.
- Added the command `/ess convert AxVaults`, allows converting the database from AxVaults to zEssentials.
- Added the command `/mailbox give <player> <item> [<amount>]`, Add an item to a player’s mailbox.
- Added the command `/mailbox giveall <player> <item> [<amount>]`, Add an item to online player’s mailbox.
- Added the command `/vault give <player> <item> [<amount>]`, Add an item to player’s vault.
- Fixed CMI convert with invalid location for homes
- Fixed the bug that allowed adding items in the mailbox even if the module is disabled.
- Fixed command /rules who are not using the correct module.
- Added the module `WorldEdit`, module allows players to have access to a **player worldedit**. They will be able to use the commands like `/pw set`, `/pw cut` to place or break blocks. Each block placed must be paid, by default 5$ per blocks. You can configure the item worldedit, the number of blocks that the player can change at the same time and many other things, more information [here](https://zessentials.groupez.dev/modules/worldedit).

# 1.0.0.4

- Added the command `/ess convert PlauerVaultX`, allows converting the database from PlayerVaultX to zEssentials.
- Added the command `/ess convert Sunlight`, allows converting the database from Sunlight to zEssentials.
- Added the command `/ess convert CoinsEngine`, allows converting the database from CoinsEngine to zEssentials.
- Items in player vault will be displayed as a single item.
- Fixed night vision messages
- Fixed duplicate lines [#33](https://github.com/Maxlego08/zEssentials/issues/33) 
- Upgrade to [Sarah](https://github.com/Maxlego08/Sarah/) version 1.10
- Fixed issue where savings by default could not be removed [#34](https://github.com/Maxlego08/zEssentials/issues/34). You must update your configuration like this
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
- Change /tp command for adding coordinate. You have now ``/tp <x> <y> <z> <yaw> <pitch>`` and ``/tp <player> <x> <y> <z> <yaw> <pitch>``
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