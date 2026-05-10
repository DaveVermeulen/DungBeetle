package org.fynixx.dungbeetle.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.fynixx.dungbeetle.DungBeetle;
import org.fynixx.dungbeetle.entity.ModEntities;
import org.fynixx.dungbeetle.entity.custom.DungBeetleEntity;

@EventBusSubscriber(modid = DungBeetle.MODID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DUNG_BEETLE.get(), DungBeetleEntity.createAttributes().build());
    }
}
