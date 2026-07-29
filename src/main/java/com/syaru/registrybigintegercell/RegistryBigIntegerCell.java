package com.syaru.registrybigintegercell;

import appeng.api.storage.StorageCells;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(RegistryBigIntegerCell.MOD_ID)
public final class RegistryBigIntegerCell {
    public static final String MOD_ID = "registry_biginteger_cell";
    private static final String LEGACY_MOD_ID = "registry_long_test_cell";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<Item> REGISTRY_TEST_CELL = ITEMS.register(
            "registry_biginteger_test_cell",
            RegistryBigIntegerTestCellItem::new);

    public static final RegistryObject<Item> CONFIGURED_TEST_CELL = ITEMS.register(
            "configured_biginteger_test_cell",
            ConfiguredBigIntegerTestCellItem::new);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB =
            CREATIVE_TABS.register(
                    "main",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable(
                                    "itemGroup.registry_biginteger_cell"))
                            .icon(() -> REGISTRY_TEST_CELL.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                // テスト用途の二種類だけを固定順で並べ、他MODのタブへ重複登録しない。
                                output.accept(REGISTRY_TEST_CELL.get());
                                output.accept(CONFIGURED_TEST_CELL.get());
                            })
                            .build());

    public RegistryBigIntegerCell() {
        StorageCells.addCellHandler(RegistryBigIntegerCellHandler.INSTANCE);
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                RegistryBigIntegerCellConfig.SPEC);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        CREATIVE_TABS.register(modBus);
        MinecraftForge.EVENT_BUS.addListener(this::remapLegacyItems);
        LOGGER.info(
                "Registry BigInteger Cell initialized: version={}, handler={}, perKeyLimit=10^{}",
                loadedVersion(),
                RegistryBigIntegerCellHandler.class.getName(),
                RegistryBigIntegerCellLimits.MAXIMUM_DECIMAL_POWER);
    }

    private static String loadedVersion() {
        return ModList.get()
                .getModContainerById(MOD_ID)
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("unknown");
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
