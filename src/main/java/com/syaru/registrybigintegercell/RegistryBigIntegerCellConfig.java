package com.syaru.registrybigintegercell;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

/** 設定対象セルへ投入するレジストリIDを定義するCommon config。 */
public final class RegistryBigIntegerCellConfig {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_IDS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> FLUID_IDS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment(
                "Configured BigInteger Test Cell contents.",
                "Each configured key receives exactly 10^64 units.",
                "Restart the server after editing this file.")
                .push("configuredCell");
        ITEM_IDS = builder
                .comment(
                        "Item registry IDs stored in the configured cell.",
                        "The public default is minecraft:cobblestone.")
                .defineListAllowEmpty(
                        "itemIds",
                        List.of("minecraft:cobblestone"),
                        RegistryBigIntegerCellConfig::isResourceLocation);
        FLUID_IDS = builder
                .comment("Fluid registry IDs stored in the configured cell.")
                .defineListAllowEmpty(
                        "fluidIds",
                        List.of(),
                        RegistryBigIntegerCellConfig::isResourceLocation);
        builder.pop();
        SPEC = builder.build();
    }

    private RegistryBigIntegerCellConfig() {
    }

    public static List<String> itemIds() {
        return ITEM_IDS.get().stream().map(String::valueOf).toList();
    }

    public static List<String> fluidIds() {
        return FLUID_IDS.get().stream().map(String::valueOf).toList();
    }

    public static boolean allows(AEKey key) {
        if (key instanceof AEItemKey itemKey) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(itemKey.getItem());
            return id != null && itemIds().contains(id.toString());
        }
        if (key instanceof AEFluidKey fluidKey) {
            ResourceLocation id = ForgeRegistries.FLUIDS.getKey(fluidKey.getFluid());
            return id != null && fluidIds().contains(id.toString());
        }
        return false;
    }

    private static boolean isResourceLocation(Object value) {
        return value instanceof String text
                && ResourceLocation.tryParse(text) != null;
    }
}
