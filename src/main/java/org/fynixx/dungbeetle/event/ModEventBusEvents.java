package org.fynixx.dungbeetle.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.fynixx.dungbeetle.Dungbeetle;
import org.fynixx.dungbeetle.entity.ModEntities;
import org.fynixx.dungbeetle.entity.client.DungBeetleModel;
import org.fynixx.dungbeetle.entity.custom.DungBeetleEntity;

@EventBusSubscriber(modid = Dungbeetle.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DungBeetleModel.LAYER_LOCATION, DungBeetleModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DUNG_BEETLE.get(), DungBeetleEntity.createAttributes().build());
    }
}
