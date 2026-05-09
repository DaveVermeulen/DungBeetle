package org.fynixx.dungbeetle.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.fynixx.dungbeetle.Dungbeetle;
import org.fynixx.dungbeetle.entity.ModEntities;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DungBeetleEntity extends Animal implements Shearable {
    private static final EntityDataAccessor<Byte> DATA_DUNG_ID = SynchedEntityData.defineId(DungBeetleEntity.class, EntityDataSerializers.BYTE);
    private static final byte DUNG_FLAG = 16;
    private static final Logger log = LoggerFactory.getLogger(DungBeetleEntity.class);
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

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
        return ModEntities.DUNG_BEETLE.get().create(level);
    }

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 100;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }
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

    @Override
    public void shear(SoundSource soundSource) {
        this.level().playSound(null, this, SoundEvents.BOGGED_SHEAR, soundSource, 1.0F, 1.0F);
        if (!this.level().isClientSide()) {
            this.setDung(false);
            this.spawnAtLocation(new ItemStack(Dungbeetle.DUNG_BALL.get()), this.getEyeHeight());
        }
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && this.hasDung() && !this.isBaby();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.GRASS_STEP;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.HONEY_BLOCK_STEP;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.HONEY_BLOCK_BREAK;
    }
}
