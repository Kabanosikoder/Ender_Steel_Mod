package net.panther.endersteel.config;

import net.fabricmc.loader.api.FabricLoader;
import net.panther.endersteel.EnderSteel;
import java.io.*;
import java.util.Properties;

public class ModConfig {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("endersteel.properties").toFile();
    private static Properties properties;

    // Armor Settings
    public static double EVASION_CHANCE = 0.45;
    public static int MAX_EVASION_CHARGES = 5;
    public static int EVASION_COOLDOWN_SECONDS = 120;

    // Enchantment Settings
    public static int PHANTOM_HARVEST_HEAL_AMOUNT = 4;
    public static float REPULSIVE_SHRIEK_DAMAGE_REFLECTION = 0.5f;
    public static float REPULSIVE_SHRIEK_KNOCKBACK = 2.0f;

    // Ore Generation Settings
    public static int ENDER_REMNANT_LARGE_VEIN_MIN_Y = 55;
    public static int ENDER_REMNANT_LARGE_VEIN_MAX_Y = 65;
    public static int ENDER_REMNANT_SMALL_VEIN_MIN_Y = 66;
    public static int ENDER_REMNANT_SMALL_VEIN_MAX_Y = 75;
    public static int ENDER_REMNANT_LARGE_VEIN_SIZE = 3;
    public static int ENDER_REMNANT_SMALL_VEIN_SIZE = 2;
    public static int ENDER_REMNANT_LARGE_VEIN_COUNT = 1;
    public static int ENDER_REMNANT_SMALL_VEIN_COUNT = 1;

    public static void loadConfig() {
        properties = new Properties();
        EnderSteel.LOGGER.info("Loading Ender Steel config from: " + CONFIG_FILE.getAbsolutePath());

        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                properties.load(reader);
                EnderSteel.LOGGER.info("Successfully loaded existing Ender Steel config");
                logCurrentValues();
            } catch (IOException e) {
                EnderSteel.LOGGER.error("Error loading Ender Steel config file", e);
                EnderSteel.LOGGER.info("Creating new config with default values");
                saveConfig(); // Create default config
            }
        } else {
            EnderSteel.LOGGER.info("No config file found, creating new one with default values");
            saveConfig(); // Create default config
        }

        // Load values from properties
        EVASION_CHANCE = getDouble("evasion_chance", 0.45);
        MAX_EVASION_CHARGES = getInt("max_evasion_charges", 5);
        EVASION_COOLDOWN_SECONDS = getInt("evasion_cooldown_seconds", 120);

        PHANTOM_HARVEST_HEAL_AMOUNT = getInt("phantom_harvest_heal_amount", 4);
        REPULSIVE_SHRIEK_DAMAGE_REFLECTION = (float) getDouble("repulsive_shriek_damage_reflection", 0.5);
        REPULSIVE_SHRIEK_KNOCKBACK = (float) getDouble("repulsive_shriek_knockback", 2.0);

        ENDER_REMNANT_LARGE_VEIN_MIN_Y = getInt("ender_remnant_large_vein_min_y", 55);
        ENDER_REMNANT_LARGE_VEIN_MAX_Y = getInt("ender_remnant_large_vein_max_y", 65);
        ENDER_REMNANT_SMALL_VEIN_MIN_Y = getInt("ender_remnant_small_vein_min_y", 66);
        ENDER_REMNANT_SMALL_VEIN_MAX_Y = getInt("ender_remnant_small_vein_max_y", 75);
        ENDER_REMNANT_LARGE_VEIN_SIZE = getInt("ender_remnant_large_vein_size", 3);
        ENDER_REMNANT_SMALL_VEIN_SIZE = getInt("ender_remnant_small_vein_size", 2);
        ENDER_REMNANT_LARGE_VEIN_COUNT = getInt("ender_remnant_large_vein_count", 1);
        ENDER_REMNANT_SMALL_VEIN_COUNT = getInt("ender_remnant_small_vein_count", 1);

        EnderSteel.LOGGER.info("Finished loading Ender Steel config");
        logCurrentValues();
    }

    public static void saveConfig() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            properties = new Properties();
            
            // Save current values to properties with comments
            properties.setProperty("evasion_chance", String.valueOf(EVASION_CHANCE));
            properties.setProperty("max_evasion_charges", String.valueOf(MAX_EVASION_CHARGES));
            properties.setProperty("evasion_cooldown_seconds", String.valueOf(EVASION_COOLDOWN_SECONDS));

            properties.setProperty("phantom_harvest_heal_amount", String.valueOf(PHANTOM_HARVEST_HEAL_AMOUNT));
            properties.setProperty("repulsive_shriek_damage_reflection", String.valueOf(REPULSIVE_SHRIEK_DAMAGE_REFLECTION));
            properties.setProperty("repulsive_shriek_knockback", String.valueOf(REPULSIVE_SHRIEK_KNOCKBACK));

            properties.setProperty("ender_remnant_large_vein_min_y", String.valueOf(ENDER_REMNANT_LARGE_VEIN_MIN_Y));
            properties.setProperty("ender_remnant_large_vein_max_y", String.valueOf(ENDER_REMNANT_LARGE_VEIN_MAX_Y));
            properties.setProperty("ender_remnant_small_vein_min_y", String.valueOf(ENDER_REMNANT_SMALL_VEIN_MIN_Y));
            properties.setProperty("ender_remnant_small_vein_max_y", String.valueOf(ENDER_REMNANT_SMALL_VEIN_MAX_Y));
            properties.setProperty("ender_remnant_large_vein_size", String.valueOf(ENDER_REMNANT_LARGE_VEIN_SIZE));
            properties.setProperty("ender_remnant_small_vein_size", String.valueOf(ENDER_REMNANT_SMALL_VEIN_SIZE));
            properties.setProperty("ender_remnant_large_vein_count", String.valueOf(ENDER_REMNANT_LARGE_VEIN_COUNT));
            properties.setProperty("ender_remnant_small_vein_count", String.valueOf(ENDER_REMNANT_SMALL_VEIN_COUNT));

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                // Remove timestamp from properties
                properties.store(writer, null);

                // Write our custom header with spacing
                try (FileWriter headerWriter = new FileWriter(CONFIG_FILE, false)) {
                    headerWriter.write("""
                    # Ender Steel Config
                    
                    #=============
                    # Armor Settings
                    #=============
                    
                    # Chance to evade an attack (0.0 to 1.0)
                    evasion_chance=0.45
                    
                    # Maximum number of evasion charges
                    max_evasion_charges=5
                    
                    # Time in seconds to restore all charges
                    evasion_cooldown_seconds=120
                    
                    #=============
                    # Enchantment Settings
                    #=============
                    
                    # Hearts restored by Phantom Harvest
                    phantom_harvest_heal_amount=4
                    
                    # Portion of damage reflected (0.0 to 1.0)
                    repulsive_shriek_damage_reflection=0.5
                    
                    # Knockback strength
                    repulsive_shriek_knockback=2.0
                    
                    #=============
                    # Ore Generation Settings
                    #=============
                    
                    # Y-level range for large veins
                    ender_remnant_large_vein_min_y=55
                    ender_remnant_large_vein_max_y=65
                    
                    # Y-level range for small veins
                    ender_remnant_small_vein_min_y=66
                    ender_remnant_small_vein_max_y=75
                    
                    # Vein sizes
                    ender_remnant_large_vein_size=3
                    ender_remnant_small_vein_size=2
                    
                    # Veins per chunk
                    ender_remnant_large_vein_count=1
                    ender_remnant_small_vein_count=1
                    """);
                }
                EnderSteel.LOGGER.info("Successfully saved Ender Steel config to: " + CONFIG_FILE.getAbsolutePath());
            }
        } catch (IOException e) {
            EnderSteel.LOGGER.error("Error saving Ender Steel config file", e);
        }
    }

    private static void logCurrentValues() {
        EnderSteel.LOGGER.info("Current Ender Steel config values:");
        EnderSteel.LOGGER.info("Armor Settings:");
        EnderSteel.LOGGER.info("  - Evasion Chance: " + EVASION_CHANCE);
        EnderSteel.LOGGER.info("  - Max Evasion Charges: " + MAX_EVASION_CHARGES);
        EnderSteel.LOGGER.info("  - Evasion Cooldown: " + EVASION_COOLDOWN_SECONDS + "s");
        
        EnderSteel.LOGGER.info("Enchantment Settings:");
        EnderSteel.LOGGER.info("  - Phantom Harvest Heal: " + PHANTOM_HARVEST_HEAL_AMOUNT);
        EnderSteel.LOGGER.info("  - Repulsive Shriek Reflection: " + REPULSIVE_SHRIEK_DAMAGE_REFLECTION);
        EnderSteel.LOGGER.info("  - Repulsive Shriek Knockback: " + REPULSIVE_SHRIEK_KNOCKBACK);
        
        EnderSteel.LOGGER.info("Ore Generation Settings:");
        EnderSteel.LOGGER.info("  - Large Veins: " + ENDER_REMNANT_LARGE_VEIN_COUNT + "x" + ENDER_REMNANT_LARGE_VEIN_SIZE + " at Y:" + ENDER_REMNANT_LARGE_VEIN_MIN_Y + "-" + ENDER_REMNANT_LARGE_VEIN_MAX_Y);
        EnderSteel.LOGGER.info("  - Small Veins: " + ENDER_REMNANT_SMALL_VEIN_COUNT + "x" + ENDER_REMNANT_SMALL_VEIN_SIZE + " at Y:" + ENDER_REMNANT_SMALL_VEIN_MIN_Y + "-" + ENDER_REMNANT_SMALL_VEIN_MAX_Y);
    }

    private static int getInt(String key, int defaultValue) {
        return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    private static double getDouble(String key, double defaultValue) {
        return Double.parseDouble(properties.getProperty(key, String.valueOf(defaultValue)));
    }
}
