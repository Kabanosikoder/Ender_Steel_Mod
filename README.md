# Ender Steel Mod

- A Minecraft mod that adds Ender infused equipment with unique abilities and enchantments.
- For 1.20.1 and 1.21.1 Fabric
- 1.21.1 is the version with the lastest features, I will not be backporting to 1.20.1

## Features

### Items
- **Ender Steel Ingot**: Crafted from Ender Scrap and used to create all Ender Steel equipment
- **Ender Scrap**: Base material created by combining Ender Pearls with Netherite Scrap
- **Ender Steel Block**: It stares back at you
- **Bagel**: A mysterious food item that is surprisingly caloric

### Equipment
- **Ender Steel Armor Set**:
  - Full set of armor (Helmet, Chestplate, Leggings, Boots)
  - Better than Netherite
  - Special Ability: 40% chance to evade attacks
  - Evasion uses charges that recharge after 2 minutes (5 total charges)
  - Some damage is undogeable such as:
    - Suffocation
    - Fall Damage
    - Kinetic (elytra)
    - /kill
    - Wither
    - Starvation
    - Void
    - Dragon Breath
    - Poison/harming
    - Direct Magic damage
    - Sweet berry bushes (would be annoying if you wasted a charge on a berry bush)
    - And thrown projectiles like eggs or snowballs (same reason as the above annoyance).

- **Ender Steel Tools**:
  - Complete tool set including Sword, Pickaxe, Axe, Shovel, Scythe, Mace.
  - Ender Steel Sword can have special enchantments such as Void Strike, Ender Streak.
    - The sword can be filled with pearls to use the Void Strike and Ender Streak enchantment.
    - The pearls will be consumed upon the use of the enchantment.
  - Ender Steel Scythe can have special enchantments such as Gazing Void, Phantom Harvest, Gravitide.

- **Void Mace**:
  - The mace doesn't currently have any unique enchantments
  - The mace can be filled with Ender Pearls or Eyes of Ender (max 4, can't be filled with both)
  - Depending on what is filled, the mace will have a special ability. (the power scales based on the amount of pearls/eyes of ender)
    - Ender Pearls: 
      - Sneak + Right click
      - Pulls entities in front of you (30 degree cone), inflicts gazing void and slowness
    - Eyes of Ender: 
      - Right click
      - Dash in whichever direction you are facing
      - Burns and inflicts gazing void on nearby entities

### Enchantments
- **Void Strike**:
  - Ender Steel Sword exclusive (Max Level: 1)
  - 50% chance to teleport hit enemies within a 5-block radius
  - Right click to with ender pearls in off hand and sword in main hand to fill up sword with pearls,
  - the pearls will be consumed upon the use of the enchantment. (same applies to the Void Mace and Ender Streak enchantment).

- **Ender Streak**:
  - Ender Steel Sword exclusive (Max Level: 3)
  - Builds up a damage multiplier streak as you hit enemies
  - Incompatible with Sharpness
  - Higher levels increase the damage bonus.

- **Gazing Void**:
  - Ender Steel Scythe exclusive (Max Level: 1)
  - Right click to activate ability while holding the scythe
  - Upon hitting an entity, the scythe will inflict blindness and the Gazing Void effect
  - While under the effect the entity will rapidly shake (affects players and mobs)
  - Compatible with Phantom Harvest and Gravitide.

- **Phantom Harvest**:
  - Ender Steel Scythe exclusive (Max Level: 1)
  - Upon killing an entity, the scythe will harvest the life of your enemy and heal you.
  - If you are wearing the full Ender Steel armor set, the scythe will refill charges upon killing an entity
  - Compatible with Gazing Void and Gravitide.

- **Gravitide**: (thanks WeakyStar for the idea for the Enchantment)
  - Ender Steel Scythe exclusive (Max Level: 2)
  - Upon hitting an entity, the Scythe will pull an enemy towards you
  - When an enemy is killed, it will pull all surrounding enemies in radius to you
  - Compatible with Phantom Harvest and Gazing Void.

- **Repulsive Shriek**:
  - Ender Steel Chestplate exclusive (Max Level: 1)
  - If you have the full Ender Steel armor set, instead of teleporting you to safety,
  - the chestplate will release a sonic boom that repels nearby mobs and reflects damage onto the attackers.
  - Final charge will do a sonic boom and damage all surrounding entities.


### Blocks
- **Ender Steel Block**:
  - Used for the Scythe crafting recipe
  - It can see you
  - Craft it for some fun interactions such as a staring contest
  - When looked at, it releases a redstone signal (strength 15), perfect for redstone doors or contraptions.
  - If you place a redstone block next to it, it's eye will be permanently open.

## Credits
- Created by Panther aka Kabanosikoder
- Coded by Panther
- Scythe (32x32 model) art by DefinitelyNotWence (https://github.com/DefinitelyNotWence)
- Bagel (the funny food item) art by RealOlive
- Gravitide enchantment idea by WeakyStar0 (https://github.com/WeakyStar0)
