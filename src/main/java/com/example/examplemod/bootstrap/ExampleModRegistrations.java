package com.example.examplemod.bootstrap;

import com.example.examplemod.registration.ModRegistrationDsl;

/**
 * このクラスを編集するだけで、MOD固有要素の登録内容を変更できます。
 * <p>
 * 重要: 初心者は「ダブルクォーテーション文字列」と「数値」だけ変更してください。
 */
public final class ExampleModRegistrations {
    private ExampleModRegistrations() {
    }

    /**
     * 要求仕様の全ゲーム要素を登録するサンプルを作成します。
     *
     * @return 登録DSL本体
     */
    public static ModRegistrationDsl createDsl() {
        ModRegistrationDsl dsl = new ModRegistrationDsl();

        // === BLOCK ===
        dsl.block("example_block", "com.example.examplemod.content.block.ExampleBlock", s -> s
                .set("hardness", 3.5)
                .set("resistance", 6.0)
                .set("creativeTab", "building_blocks"));

        // === ENTITY ===
        dsl.entity("example_entity", "com.example.examplemod.content.entity.ExampleEntity", s -> s
                .set("width", 0.8)
                .set("height", 1.95)
                .set("trackingRange", 64));

        // === BLOCK ENTITY ===
        dsl.blockEntity("example_block_entity", "com.example.examplemod.content.blockentity.ExampleBlockEntity", s -> s
                .set("attachedBlock", "example_block")
                .set("tickable", true));

        // === CLIENT ===
        dsl.client("example_client", "com.example.examplemod.client.ExampleClientBootstrap", s -> s
                .set("renderer", "com.example.examplemod.client.render.ExampleRenderer")
                .set("keybind", "key.examplemod.open_gui"));

        // === GUI ===
        dsl.gui("example_screen", "com.example.examplemod.client.gui.ExampleScreen", s -> s
                .set("title", "Example Machine")
                .set("width", 176)
                .set("height", 166));

        // === DIMENSION ===
        dsl.dimension("example_dimension", "com.example.examplemod.world.dimension.ExampleDimension", s -> s
                .set("ambientLight", 0.2)
                .set("fixedTime", 18000));

        // === BIOME ===
        dsl.biome("example_biome", "com.example.examplemod.world.biome.ExampleBiome", s -> s
                .set("temperature", 0.8)
                .set("downfall", 0.4)
                .set("skyColor", 7907327));

        // === ITEM ===
        dsl.item("example_item", "com.example.examplemod.content.item.ExampleItem", s -> s
                .set("maxStackSize", 64)
                .set("rarity", "uncommon"));

        // === PACKET ===
        dsl.packet("example_sync_packet", "com.example.examplemod.network.ExampleSyncPacket", s -> s
                .set("direction", "S2C")
                .set("channel", "examplemod:sync"));

        // === DATAGEN ===
        dsl.dataGen("example_datagen", "com.example.examplemod.datagen.ExampleDataGenerator", s -> s
                .set("output", "generated/resources")
                .set("includeLang", true));

        // === CUSTOM (DSLに無い要素の拡張) ===
        dsl.custom("spell", "example_fire_spell", s -> s
                .impl("com.example.examplemod.magic.ExampleFireSpell")
                .set("mana", 30)
                .set("cooldownTicks", 40));

        return dsl;
    }
}
