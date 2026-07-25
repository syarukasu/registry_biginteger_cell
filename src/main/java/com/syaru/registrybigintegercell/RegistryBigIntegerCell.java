package com.syaru.registrybigintegercell;

import appeng.api.storage.StorageCells;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod(RegistryBigIntegerCell.MOD_ID)
public final class RegistryBigIntegerCell {
    public static final String MOD_ID = "registry_biginteger_cell";
    private static final String LEGACY_MOD_ID = "registry_long_test_cell";

    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> REGISTRY_TEST_CELL = ITEMS.register(
            "registry_biginteger_test_cell",
            RegistryBigIntegerTestCellItem::new);

    public static final RegistryObject<Item> CONFIGURED_TEST_CELL = ITEMS.register(
            "configured_biginteger_test_cell",
            ConfiguredBigIntegerTestCellItem::new);

    public RegistryBigIntegerCell() {
        StorageCells.addCellHandler(RegistryBigIntegerCellHandler.INSTANCE);
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                RegistryBigIntegerCellConfig.SPEC);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        modBus.addListener(this::addCreativeTabContents);
        MinecraftForge.EVENT_BUS.addListener(this::remapLegacyItems);
    }

    private void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(REGISTRY_TEST_CELL);
            event.accept(CONFIGURED_TEST_CELL);
        }
    }

    private void remapLegacyItems(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Item> mapping :
                event.getMappings(Registries.ITEM, LEGACY_MOD_ID)) {
            switch (mapping.getKey().getPath()) {
                case "registry_biginteger_test_cell" ->
                        mapping.remap(REGISTRY_TEST_CELL.get());
                case "configured_biginteger_test_cell" ->
                        mapping.remap(CONFIGURED_TEST_CELL.get());
                default -> {
                }
            }
        }
    }
}
