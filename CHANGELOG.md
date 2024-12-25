# Changelog

All notable changes to the Ender Steel Mod will be documented in this file.

## [1.0.1] - 2024-12-25

### Added
- New Ender's Edge enchantment for Ender Steel Sword
  - 50% chance to teleport enemies within 5 blocks
  - Works alongside the pearl storage system
  - Uses stored pearls for teleportation
- Safe teleportation system
  - Prevents entities from teleporting into walls
  - Ensures solid ground beneath teleport location
  - Requires 2 blocks of air space for entity height

### Changed
- Updated Ender Steel Sword mechanics
  - Pearl storage now works with Ender's Edge enchantment
  - Tooltip always shows stored pearl count
  - Improved teleportation effects and particles
- Enhanced tooltips and descriptions
  - More detailed enchantment descriptions
  - Clearer ability explanations

### Fixed
- Teleportation safety checks
  - Entities no longer teleport into unsafe locations
  - Failed teleports don't consume pearls

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
- Implemented custom rendering for status effects
- Added NBT data handling for pearl storage
- Created custom armor event system for dodge ability
