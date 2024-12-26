# Changelog

All notable changes to the Ender Steel Mod will be documented in this file.

## [1.0.3] - 2024-12-26

### Changed
- Modified Ender Steel Armor evasion ability
  - Added 40% chance to trigger evasion when hit
  - When charges reach zero, enters 2-minute cooldown
  - After cooldown, all 5 charges are restored at once
  - Does not activate when blocking with shield
  - More balanced with chance-based activation
- Adjusted Ender Steel Block behavior
  - Added quarter-second delay between state transitions
  - Smoother opening and closing animations
  - More predictable block state changes
- Added bagel to Ender Steel creative tab

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
- Renamed "Ender's Edge" to "Ender Strike" for consistency
- Rebalanced Ender Strike enchantment
  - Now a single-level enchantment
  - Maintains 50% chance to teleport enemies
  - Cannot be combined with Ender Streak
- Updated tooltips to show:
  - Current streak counter
  - Current bonus damage
  - Stored pearl count

### Technical
- Improved enchantment compatibility system
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

## [1.0.0] - 2024-12-25

### Added
- Initial release of the Ender Steel Mod
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
  - Recipe yields 2 bagels

### Technical
- Built for Minecraft 1.20.1
- Requires Fabric Loader and Fabric API