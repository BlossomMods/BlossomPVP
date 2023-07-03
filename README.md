# BlossomPVP

BlossomPVP is a Minecraft Fabric mod in the Blossom-series mods that provides /pvp command

## Table of contents

- [Dependencies](#dependencies)
- [Config](#config)
- [Commands & their permissions](#commands--their-permissions)
- [Translation keys](#translation-keys)

## Dependencies

* [BlossomLib](https://github.com/BlossomMods/BlossomLib)
* [fabric-permissions-api](https://github.com/lucko/fabric-permissions-api) / [LuckPerms](https://luckperms.net/) /
  etc. (Optional)

## Config

This mod's config file can be found at `config/BlossomMods/BlossomPVP.json`, after running the server with
the mod at least once.

`enabledByDefault`: boolean - whether PVP is enabled for all players by default  
`defaultActionIsQuery`: boolean - whether the command, when executed with no parameters, queries or toggles PVP

## Commands & their permissions

- `/pvp` - toggles PVP state; if `defaultActionIsQuery` is `true`, gets PVP state instead  
  Permission: `blossom.pvp` (default: true)
  - `true` - enables PVP
  - `false` - disabled PVP
  - `query` - gets current PVP state
    - `<player>` - gets PVP state of player
      Permission: `blossom.pvp.query-player` (default: true)

## Translation keys

Only keys with available arguments are shown, for full list, please see
[`src/main/resources/data/blossom/lang/en_us.json`](src/main/resources/data/blossom/lang/en_us.json)

- `blossom.pvp.query.player.enabled`: 1 argument - Player
- `blossom.pvp.query.player.disabled`: 1 argument - Player
- `blossom.pvp.fail.both.attacker`: 1 argument - Player, who got attacked (defender)
- `blossom.pvp.fail.both.defender`: 1 argument - Player, who attacked (attacker)
- `blossom.pvp.fail.attacker.attacker`: 1 argument - Player, who got attacked (defender)
- `blossom.pvp.fail.attacker.defender`: 1 argument - Player, who attacked (attacker)
- `blossom.pvp.fail.defender.attacker`: 1 argument - Player, who got attacked (defender)
- `blossom.pvp.fail.defender.defender`: 1 argument - Player, who attacked (attacker)

Notes:

* `blossom.pvp.fail.both.attacker` and `blossom.pvp.fail.both.defender` are returned when both the attacker and defender
  have PVP disabled;
* `blossom.pvp.fail.attacker.attacker` and `blossom.pvp.fail.attacker.defender` are returned when only the attacker has
  PVP disabled;
* `blossom.pvp.fail.defender.attacker` and `blossom.pvp.fail.defender.defender` are returned when only the defender has
  PVP disabled.

To disable any of the `blossom.pvp.fail.*` keys from being sent, they can be set to an empty string (`""`).
