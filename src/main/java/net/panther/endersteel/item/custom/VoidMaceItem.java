package net.panther.endersteel.item.custom;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.panther.endersteel.component.EnderSteelDataComponents;
import net.panther.endersteel.effect.ModEffects;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class VoidMaceItem extends MaceItem {
    private static final int DASH_COOLDOWN = 140; // 20 ticks = 1 second
    private static final float DASH_SPEED = 0.5f;
    private static final float DASH_RANGE = 3.75f;
    private static final float EFFECT_RADIUS = 6.0f;

    private static final float PULL_RANGE = 20.0f; // Maximum range for pulling
    private static final float PULL_STRENGTH = 1.25f; // Base strength of the pull
    private static final int GROUND_DURATION = 60; // Base duration of grounding effect in ticks (3 seconds)

    private static final int MAX_SOCKETS = 4;
    private static final String EYE_TYPE = "eye";
    private static final String PEARL_TYPE = "pearl";
    private final ToolMaterial material;

    public VoidMaceItem(ToolMaterial material, Settings settings) {
        super(settings.attributeModifiers(MaceItem.createAttributeModifiers()));
        this.material = material;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        int filledSockets = getFilledSockets(stack);
        String socketType = getSocketType(stack);

        tooltip.add(Text.translatable("tooltip.void_mace.sockets", filledSockets, MAX_SOCKETS)
                .formatted(Formatting.GRAY));

        if (socketType.isEmpty()) {
            tooltip.add(Text.translatable("tooltip.void_mace.unsocketed").formatted(Formatting.DARK_GRAY));
        } else {
            String typeKey = "tooltip.void_mace.socket_type." + socketType;
            tooltip.add(Text.translatable(typeKey, filledSockets).formatted(Formatting.DARK_PURPLE));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.isSneaking() && hand == Hand.MAIN_HAND) {
            // Only activate if the mace has ender pearl sockets
            if (PEARL_TYPE.equals(getSocketType(stack)) && getFilledSockets(stack) > 0) {
                if (!world.isClient()) {
                    pullAndGroundEntities(user, stack);
                }
                user.getItemCooldownManager().set(this, 60); // 3 second cooldown
                return TypedActionResult.success(stack);
            }
            return TypedActionResult.pass(stack);
        }

        // Existing socket/dash logic
        if (!world.isClient && hand == Hand.MAIN_HAND) {
            ItemStack offhandStack = user.getOffHandStack();
            if (offhandStack.isOf(Items.ENDER_EYE) || offhandStack.isOf(Items.ENDER_PEARL)) {
                String type = offhandStack.isOf(Items.ENDER_EYE) ? EYE_TYPE : PEARL_TYPE;
                if (addSocket(stack, type, user)) {
                    offhandStack.decrement(1);
                    user.sendMessage(Text.translatable("message.void_mace.socket_added",
                        type.equals(EYE_TYPE) ? "Eye of Ender" : "Ender Pearl"), true);
                    world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        SoundEvents.ITEM_LODESTONE_COMPASS_LOCK,
                        SoundCategory.PLAYERS,
                        1.0f, 1.0f);
                }
                return TypedActionResult.success(stack);
            }
        }

        if (EYE_TYPE.equals(getSocketType(stack))) {
            int sockets = getFilledSockets(stack);
            if (sockets > 0) {
                performDash(user, stack);

                stack.set(EnderSteelDataComponents.VOID_MACE_SOCKETS, sockets - 1);

                world.playSound(
                    null,
                    user.getX(),
                    user.getY(),
                    user.getZ(),
                    SoundEvents.ENTITY_ENDER_EYE_DEATH,
                    SoundCategory.PLAYERS,
                    1.25f,
                    0.25f
                );
                return TypedActionResult.success(stack);
            }
        }

        return super.use(world, user, hand);
    }

    private void performDash(PlayerEntity player, ItemStack stack) {
        if (player.getItemCooldownManager().isCoolingDown(this)) return;

        Vec3d look = player.getRotationVector().normalize();

        // Dash velocity
        Vec3d velocity = look.multiply(DASH_SPEED * DASH_RANGE);
        player.setVelocity(velocity);
        player.velocityModified = true;

        player.getWorld().playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
            SoundCategory.PLAYERS,
            0.75f,
            0.25f
        );

        player.getItemCooldownManager().set(this, DASH_COOLDOWN);

        if (!player.getWorld().isClient) {
            spawnDashParticles(player.getWorld(), player.getPos(), look);
            affectEntitiesInPath(player, player.getPos(), look);
        }
    }

    private void spawnDashParticles(World world, Vec3d startPos, Vec3d direction) {
        if (world instanceof ServerWorld serverWorld) {
            for (int i = 0; i < 10; i++) {
                double progress = i / 10.0 * DASH_RANGE;
                Vec3d pos = startPos.add(direction.multiply(progress));

                serverWorld.spawnParticles(
                    ParticleTypes.DRAGON_BREATH,
                    pos.x,
                    pos.y + 1.0,
                    pos.z,
                    5,
                    0.2, 0.2, 0.2,
                    0.02
                );

                serverWorld.spawnParticles(
                    ParticleTypes.FLAME,
                    pos.x,
                    pos.y + 1.0,
                    pos.z,
                    3,
                    0.1, 0.1, 0.1,
                    0.05
                );
            }
        }
    }

    private void affectEntitiesInPath(LivingEntity source, Vec3d startPos, Vec3d direction) {
        World world = source.getWorld();
        Box box = new Box(
            startPos.x - EFFECT_RADIUS,
            startPos.y - 1.0,
            startPos.z - EFFECT_RADIUS,
            startPos.x + EFFECT_RADIUS,
            startPos.y + 2.0,
            startPos.z + EFFECT_RADIUS
        ).stretch(direction.multiply(DASH_RANGE));

        for (LivingEntity entity : world.getEntitiesByClass(
            LivingEntity.class,
            box,
            e -> e != source && e.isAlive()
        )) {
            entity.setOnFireFor(6); // 3 seconds of fire
            entity.addStatusEffect(new StatusEffectInstance(ModEffects.GAZING_VOID, 60, 0));

            Vec3d away = entity.getPos().subtract(startPos).normalize().multiply(0.5);
            entity.addVelocity(away.x, 0.2, away.z);
        }
    }

    private void pullAndGroundEntities(PlayerEntity user, ItemStack stack) {
        World world = user.getWorld();
        int sockets = getFilledSockets(stack);

        if (sockets > 0) {
            stack.set(EnderSteelDataComponents.VOID_MACE_SOCKETS, sockets - 1);
        } else {
            return;
        }

        // Calculate the end position of the raycast
        Vec3d startPos = user.getEyePos();
        Vec3d lookVec = user.getRotationVec(1.0f);
        Vec3d endPos = startPos.add(lookVec.multiply(PULL_RANGE));

        // Raycast
        HitResult hitResult = world.raycast(new RaycastContext(
            startPos, endPos,
            RaycastContext.ShapeType.COLLIDER,
            RaycastContext.FluidHandling.NONE,
            user
        ));

        // Get entities in a cone in front of the player
        Vec3d lookVecHor = new Vec3d(lookVec.x, 0, lookVec.z).normalize();
        double coneAngle = Math.toRadians(30); // 30 degree cone

        List<LivingEntity> entities = world.getEntitiesByClass(
            LivingEntity.class,
            user.getBoundingBox().stretch(lookVec.multiply(PULL_RANGE)).expand(1.0, 2.0, 1.0),
            entity -> {
                if (entity == user || !entity.isAlive()) return false;

                // Is entity is within the cone?
                Vec3d toEntity = entity.getPos().subtract(startPos).normalize();
                double angle = Math.acos(toEntity.normalize().dotProduct(lookVec));

                return angle < coneAngle && entity.distanceTo(user) < PULL_RANGE;
            }
        );

        if (!entities.isEmpty()) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS,
                1.0f, 0.01f);

            for (LivingEntity entity : entities) {
                Vec3d pullVec = user.getPos().subtract(entity.getPos()).normalize()
                    .multiply(PULL_STRENGTH * (1 + sockets * 0.2)); // Scales with socket count

                entity.setVelocity(pullVec);
                entity.velocityModified = true;

                int groundDuration = GROUND_DURATION + (sockets * 30); // 1.5s per socket

                entity.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    groundDuration,
                    3,
                    false,
                    true,
                    true
                ));

                entity.addStatusEffect(new StatusEffectInstance(
                    ModEffects.GAZING_VOID,
                    groundDuration,
                    0,
                    false,
                    true,
                    true
                ));

                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(
                        ParticleTypes.REVERSE_PORTAL,
                        entity.getX(), entity.getY() + entity.getHeight()/2, entity.getZ(),
                        10, 0.5, 0.5, 0.5, 0.1
                    );
                }
            }
        }
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 1000;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantability() {
        return this.material.getEnchantability();
    }

    public static int getFilledSockets(ItemStack stack) {
        return stack.getOrDefault(EnderSteelDataComponents.VOID_MACE_SOCKETS, 0);
    }

    public static String getSocketType(ItemStack stack) {
        return stack.getOrDefault(EnderSteelDataComponents.SOCKET_TYPE, "");
    }

    public static boolean canAddSocket(ItemStack stack, String type) {
        int currentSockets = getFilledSockets(stack);
        String currentType = getSocketType(stack);
        return currentSockets < MAX_SOCKETS && (currentSockets == 0 || currentType.isEmpty() || currentType.equals(type));
    }

    public static boolean addSocket(ItemStack stack, String type, @Nullable PlayerEntity player) {
        if (!canAddSocket(stack, type)) {
            String currentType = getSocketType(stack);
            if (!currentType.isEmpty() && !currentType.equals(type) && player != null) {
                player.sendMessage(Text.translatable("message.void_mace.wrong_socket_type")
                    .formatted(Formatting.RED), true);
            }
            return false;
        }
        
        int currentSockets = getFilledSockets(stack);
        stack.set(EnderSteelDataComponents.VOID_MACE_SOCKETS, currentSockets + 1);
        stack.set(EnderSteelDataComponents.SOCKET_TYPE, type);
        
        if (player != null) {
            player.playSound(SoundEvents.ITEM_LODESTONE_COMPASS_LOCK
                , 0.5f, 1.0f);
        }
        return true;
    }
}