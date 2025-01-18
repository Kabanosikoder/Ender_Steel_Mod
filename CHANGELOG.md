# Changelog

All notable changes to the Ender Steel Mod will be documented in this file.

## [1.1.2] January 18, 2025

### Added
- Added Ender Remnant ore
  - Generates in End islands between Y=55 and Y=75
  - Requires netherite-tier tools or higher to mine
  - Generates in two patterns:
    - Large veins (3 blocks) at Y=55-65
    - Small veins (1 blocks) at Y=66-75
  - Hidden in End Stone
  - Can be smelted into Ender Scrap
- Added redstone functionality to Ender Steel Block
  - Eye opens and stays open when a redstone block is adjacent
  - Eye closes when redstone block is removed (unless being looked at)
  - Can still interact with the eye normally through player staring
- Added smithing tempalte
  - Smithing recipes for each netherite armor/tool except the hoe (scythe)

### Fixed
- Updated mining level requirements
  - Ender Steel tools are now properly tagged as level 5 tools
- Fixed Ender Steel Block drop, it actually drops itself

## [1.1.1] January 11, 2025

### Added
- New Advancements System
  - Added "Enter the Void" root advancement
    - Triggered by obtaining Ender Scrap
    - Uses end stone background texture
  - Added "Void Walker" advancement
    - Triggered by obtaining a complete set of Ender Steel armor
  - Added "Staring Contest" advancement
    - Triggered by staring at an Ender Steel Block with a fully open eye for 2 minutes
  - Added "Echo of the End" advancement
    - Triggered by successfully dodging an attack using the Ender Steel armor's evasion ability
  - Added "Endermite's Bane" advancement
    - Triggered by defeating an Endermite using any Ender Steel weapon

- Increased range for staring at the Ender Steel Block from 5 to 20 blocks
- Added new enchantments: Phantom Harvest which on kill recharges evasion charges when wearing full armor set or heals 1 heart if the player is not wearing the full armor set.
- Added new enchant for the Ender Steel Chestplate: Repulsive Shriek, repulses enemies when you are hit and reflects damage back to them, last charge releases a super powerful shriek
- Have fun using these two enchantments together during PvP

## [1.1.0] - 2024-12-26

### Changed
- Modified Ender Steel Armor evasion ability
  - Added 40% chance to trigger evasion when hit
  - When charges reach zero, enters 2-minute cooldown
  - After cooldown, all 5 charges are restored at once
  - Does not activate when blocking with shield
- Adjusted Ender Steel Block behavior
- Added bagel to Ender Steel creative tab
- Ender Strike renamed to Void Strike for ++coolness factor

## [1.0.2] - 2024-12-26

### Added
- New Ender Streak enchantment for Ender Steel Sword
  - Max Level III
  - 15% chance to trigger bonus damage based on streak
  - Damage increases with streak and enchantment level:
    - Level I: +0.5 damage per streak
    - Level II: +1.0 damage per streak
    - Level III: +1.5 damage per streak
  - Streak persists until death or switching items
  - Uses stored pearls for activation
  - Cannot be combined with Ender Strike

### Changed
- Renamed "Ender's Edge" to "Ender Strike" for obvious reasons
- Rebalanced Ender Strike enchantment
  - Now a single-level enchantment
  - Maintains 50% chance to teleport enemies
  - Cannot be combined with Ender Streak
- Updated tooltips to show:
  - Current streak counter
  - Current bonus damage
  - Stored pearl count

### Extra
- Improved enchantments
- Enhanced damage calculation for streak system
- Added persistent streak tracking using NBT data

## [1.0.1] - 2024-12-25

### Added
- New Ender's Edge enchantment for Ender Steel Sword
  - 50% chance to teleport enemies within 5 blocks
  - Works alongside the pearl storage system
  - Uses stored pearls for teleportation
- Safe teleportation system
  - Prevents entities from teleporting into walls
  - Ensures solid ground beneath teleport location
  - Requires 2 blocks of airspace for entity height

### Changed
- Updated Ender Steel Sword mechanics
  - Pearl storage now works with Ender's Edge enchantment
  - Tooltip always shows stored pearl count
  - Improved teleportation effects and particles (dragons breath particles now for extra coolness)
- Enhanced tooltips and descriptions
  - More detailed enchantment descriptions
  - Clearer ability explanations

### Fixed
- Teleportation safety checks
  - Entities no longer teleport into unsafe locations (such as walls or 69 blocks underground)
  - Failed teleports don't consume pearls anymore

## [1.0.0] - 2024-12-23

### Added
- Initial build of the Ender Steel Mod
- Ender Steel Armor Set with dodge ability
- Ender Steel Sword with pearl storage system
- Ender Steel Scythe with Gazing Void enchantment
- Basic tools (Pickaxe, Axe, Shovel)
- Crafting materials (Ender Scrap, Ender Steel Ingot)
- Ender Steel Block
- Custom status effects and enchantments
- Visual and audio feedback for all abilities
- Added Bagel food item
  - Restores 2 hunger bars with 0.8 saturation
  - Crafted with 4 wheat in a circle and wheat seeds in the middle
