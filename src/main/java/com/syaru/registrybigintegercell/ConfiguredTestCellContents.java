package com.syaru.registrybigintegercell;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import com.extendedae_plus.ExtendedAEPlus;
import com.extendedae_plus.util.storage.InfinityDataStorage;
import com.extendedae_plus.util.storage.InfinityStorageManager;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

public final class ConfiguredTestCellContents {
    public static final BigInteger AMOUNT =
            RegistryBigIntegerCellLimits.MAXIMUM_PER_KEY;

    private static final int DATA_VERSION = 2;
    private static final String UUID_TAG = "infinity_cell_uuid";
    private static final String VERSION_TAG = "configured_test_cell_version";
    private static final String CONFIG_HASH_TAG = "configured_test_cell_hash";
    private static final String ITEM_COUNT_TAG = "configured_test_cell_item_keys";
    private static final String FLUID_COUNT_TAG = "configured_test_cell_fluid_keys";

    private ConfiguredTestCellContents() {
    }

    public static InitializationResult ensureInitialized(
            ItemStack stack,
            MinecraftServer server,
            boolean force) {
        CompoundTag tag = stack.getOrCreateTag();
        InfinityStorageManager manager = InfinityStorageManager.getInstance(server);
        ExtendedAEPlus.STORAGE_INSTANCE = manager;

        UUID uuid = tag.hasUUID(UUID_TAG)
                ? tag.getUUID(UUID_TAG)
                : UUID.randomUUID();
        tag.putUUID(UUID_TAG, uuid);

        int configHash = Objects.hash(
                RegistryBigIntegerCellConfig.itemIds(),
                RegistryBigIntegerCellConfig.fluidIds());
        if (!force
                && tag.getInt(VERSION_TAG) == DATA_VERSION
                && tag.getInt(CONFIG_HASH_TAG) == configHash
                && manager.hasUUID(uuid)) {
            return readSummary(stack).orElse(new InitializationResult(0, 0));
        }

        Map<AEKey, BigInteger> amounts = new LinkedHashMap<>();
        int itemKeys = addItems(amounts);
        int fluidKeys = addFluids(amounts);

        InfinityDataStorage storage = new InfinityDataStorage();
        storage.amounts.putAll(amounts);
        storage.itemCount = AMOUNT.multiply(BigInteger.valueOf(amounts.size()));
        manager.updateCell(uuid, storage);

        tag.putInt(VERSION_TAG, DATA_VERSION);
        tag.putInt(CONFIG_HASH_TAG, configHash);
        tag.putInt(ITEM_COUNT_TAG, itemKeys);
        tag.putInt(FLUID_COUNT_TAG, fluidKeys);
        byte[] total = storage.itemCount.toByteArray();
        tag.putByteArray("infinity_item_total", total);
        tag.putByteArray("infinity_cell_item_count", total);
        tag.putInt("infinity_item_types", amounts.size());
        return new InitializationResult(itemKeys, fluidKeys);
    }

    public static Optional<InitializationResult> readSummary(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || tag.getInt(VERSION_TAG) != DATA_VERSION) {
            return Optional.empty();
        }
        return Optional.of(new InitializationResult(
                tag.getInt(ITEM_COUNT_TAG),
                tag.getInt(FLUID_COUNT_TAG)));
    }

    private static int addItems(Map<AEKey, BigInteger> amounts) {
        int before = amounts.size();
        for (String text : RegistryBigIntegerCellConfig.itemIds()) {
            ResourceLocation id = ResourceLocation.tryParse(text);
            Item item = id == null ? null : ForgeRegistries.ITEMS.getValue(id);
            if (item == null || item == Items.AIR) {
                continue;
            }
            AEItemKey key = AEItemKey.of(item);
            if (key != null) {
                amounts.put(key, AMOUNT);
            }
        }
        return amounts.size() - before;
    }

    private static int addFluids(Map<AEKey, BigInteger> amounts) {
        int before = amounts.size();
        for (String text : RegistryBigIntegerCellConfig.fluidIds()) {
            ResourceLocation id = ResourceLocation.tryParse(text);
            Fluid fluid = id == null ? null : ForgeRegistries.FLUIDS.getValue(id);
            if (fluid == null || fluid == Fluids.EMPTY) {
                continue;
            }
            AEFluidKey key = AEFluidKey.of(fluid);
            if (key != null) {
                amounts.put(key, AMOUNT);
            }
        }
        return amounts.size() - before;
    }

    public record InitializationResult(int itemKeys, int fluidKeys) {
    }
}
