package org.fynixx.dungbeetle.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.fynixx.dungbeetle.Dungbeetle;
import org.fynixx.dungbeetle.entity.ModEntities;

public class DungBeetleItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Dungbeetle.MODID);

    public static final DeferredItem<Item> DUNG_BALL = ITEMS.register("dung_ball",
                    () -> new BoneMealItem(new Item.Properties().food(new FoodProperties.Builder()
                            .nutrition(1)
                            .effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 0.6F)
                            .effect(new MobEffectInstance(MobEffects.HUNGER, 100, 0), 0.6F)
                            .build()
                    )));

    public static final DeferredItem<Item> CREATIVE_ICON = ITEMS.register("creative_icon",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DUNG_BEETLE_SPAWN_EGG = ITEMS.register("dung_beetle_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.DUNG_BEETLE, 0xffffff, 0xffffff,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
