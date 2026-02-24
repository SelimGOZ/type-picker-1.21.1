package net.fortuneblack05.typerando.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TypePickerItems {
    public static final Item FAKE_TYPE_SHARD = Registry.register(
            Registries.ITEM,
            Identifier.of("typerando", "faketype_shard"),
            new Item(new Item.Settings().maxCount(1))
    );

    public static void register() {

    }
}
