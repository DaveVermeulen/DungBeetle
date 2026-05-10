package org.fynixx.dungbeetle.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.fynixx.dungbeetle.DungBeetle;
import org.fynixx.dungbeetle.item.DungBeetleItems;

import java.util.function.Supplier;

public class DungBeetleBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DungBeetle.MODID);

    public static final DeferredBlock<Block> DUNG_BLOCK = registerBlock("dung_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.HONEY_BLOCK)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        DungBeetleItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
