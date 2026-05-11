package org.fynixx.dungbeetle;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.fynixx.dungbeetle.block.DungBeetleBlocks;
import org.fynixx.dungbeetle.entity.ModEntities;
import org.fynixx.dungbeetle.entity.client.DungBeetleRenderer;
import org.fynixx.dungbeetle.entity.custom.DungBeetleEntity;
import org.fynixx.dungbeetle.item.DungBeetleItems;
import org.fynixx.dungbeetle.registry.DungBeetleBiomeModifiers;
import org.fynixx.dungbeetle.sound.DungBeetleSounds;
import org.slf4j.Logger;
import software.bernie.geckolib.loading.math.MolangQueries;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DungBeetle.MODID)
public class DungBeetle {
    public static final String MODID = "dungbeetle";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DUNG_TAB = CREATIVE_MODE_TABS.register("dung_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.dungbeetle"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> DungBeetleItems.CREATIVE_ICON.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {

                        output.accept(DungBeetleItems.DUNG_BALL);
                        output.accept(DungBeetleBlocks.DUNG_BLOCK);
                        output.accept(DungBeetleItems.DUNG_BEETLE_SPAWN_EGG);

    }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public DungBeetle(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerSpawnPlacements);

        DungBeetleBlocks.register(modEventBus);
        DungBeetleItems.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        ModEntities.register(modEventBus);
        DungBeetleSounds.register(modEventBus);

        DungBeetleBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);

        MolangQueries.<DungBeetleEntity>setActorVariable("query.dungbeetle_dung_rotation",
                actor -> actor.animatable().getDungRotation());

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Dungbeetle) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void  registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.DUNG_BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.DUNG_BEETLE.get(), DungBeetleRenderer::new);
        }
    }
}
