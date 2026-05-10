package org.fynixx.dungbeetle.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.fynixx.dungbeetle.entity.ModEntities;
import org.fynixx.dungbeetle.item.DungBeetleItems;
import org.fynixx.dungbeetle.sound.DungBeetleSounds;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.ArrayList;
import java.util.List;

public class DungBeetleEntity extends Animal implements Shearable, GeoEntity {
    private static final EntityDataAccessor<Byte> DATA_DUNG_ID = SynchedEntityData.defineId(DungBeetleEntity.class, EntityDataSerializers.BYTE);
    private static final byte DUNG_FLAG = 16;
    private static final Logger log = LoggerFactory.getLogger(DungBeetleEntity.class);
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private float dungRotation = 0.0F;
    private static final float ROTATION_SPEED = 5.0F;

    public DungBeetleEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, itemStack -> itemStack.is(Items.BROWN_DYE), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new FindDungFromAnimalGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 6d)
                .add(Attributes.MOVEMENT_SPEED, 0.125D)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.BROWN_DYE);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        DungBeetleEntity babyEntity = ModEntities.DUNG_BEETLE.get().create(level);
        babyEntity.setDung(false);
        return babyEntity;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            this.dungRotation = (this.dungRotation - ROTATION_SPEED) % -360.0F;
//            System.out.println("dungRotation: " + this.dungRotation);
        }
    }

    public float getDungRotation() {
        return this.dungRotation;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DUNG_ID, (byte)16);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Dung", this.hasDung());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Dung")) {
            this.setDung(tag.getBoolean("Dung"));
        }
    }

    public boolean hasDung() {
        return (this.entityData.get(DATA_DUNG_ID) & 16) != 0;
    }

    public void setDung(boolean dungEquipped) {
        byte b0 = this.entityData.get(DATA_DUNG_ID);
        if (dungEquipped) {
            this.entityData.set(DATA_DUNG_ID, (byte)(b0 | 16));
        } else {
            this.entityData.set(DATA_DUNG_ID, (byte)(b0 & -17));
        }
    }

    public void addDungToBeetle() {
        this.setDung(true);
        this.level().playSound(null, this, SoundEvents.FROG_LAY_SPAWN, SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(DungBeetleItems.DUNG_BALL.get())) {
            if (!this.level().isClientSide && !hasDung()) {
                addDungToBeetle();
                itemStack.setCount(itemStack.getCount() - 1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && this.hasDung() && !this.isBaby();
    }

    @Override
    public void shear(SoundSource soundSource) {
        this.level().playSound(null, this, SoundEvents.BOGGED_SHEAR, soundSource, 1.0F, 1.0F);
        if (!this.level().isClientSide()) {
            this.setDung(false);
            this.spawnAtLocation(new ItemStack(DungBeetleItems.DUNG_BALL.get()), this.getEyeHeight());
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return DungBeetleSounds.DUNG_BEETLE_IDLE.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BEE_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    public String animationType() {return hasDung() ? "" : "_DUNGLESS";}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(software.bernie.geckolib.animation.AnimationState<DungBeetleEntity> dungBeetleEntityAnimationState) {
        if(this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
                dungBeetleEntityAnimationState.getController().setAnimation(RawAnimation.begin().thenLoop(("ANIM_DUNG_BEETLE_WALK" + animationType())));
        } else {
            dungBeetleEntityAnimationState.getController().setAnimation(RawAnimation.begin().thenLoop(("ANIM_DUNG_BEETLE_IDLE" + animationType())));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    static class FindDungFromAnimalGoal extends Goal {
        private final DungBeetleEntity dungBeetle;
        private List<Animal> animals = new ArrayList<>();
        private Animal nearestAnimal;
        private int timeToRecalcPath;

        public FindDungFromAnimalGoal(DungBeetleEntity dungBeetle) {
            this.dungBeetle = dungBeetle;
        }

        @Override
        public boolean canUse() {
            this.animals = this.dungBeetle.level().getEntitiesOfClass(
                    Animal.class,
                    this.dungBeetle.getBoundingBox().inflate(5.0D, 5.0D, 5.0D),
                    e -> e != this.dungBeetle && !e.isBaby() && (e.getClass() != DungBeetleEntity.class)
            );
            return !animals.isEmpty() && !this.dungBeetle.hasDung() && this.dungBeetle.getRandom().nextFloat() < 0.1F;
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
        }

        @Override
        public void stop() {
            this.animals.clear();
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.nearestAnimal = getNearestAnimal();
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                this.dungBeetle.getNavigation().moveTo(this.nearestAnimal, 1.5);
            }

            if(this.dungBeetle.getNavigation().isDone()) {
                this.dungBeetle.lookAt(this.nearestAnimal, 360, 360);
                this.dungBeetle.addDungToBeetle();
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() { return true; }

        public Animal getNearestAnimal() {
            Animal nearestAnimal = animals.getFirst();
            for (Animal e : this.animals) {
                if (e.position().distanceTo(this.dungBeetle.position()) <= nearestAnimal.position().distanceTo(this.dungBeetle.position())) {
                    nearestAnimal = e;
                }
            }
            return nearestAnimal;
        }
    }
}
