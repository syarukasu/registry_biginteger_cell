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
import java.util.Optional;
import java.util.UUID;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public final class RegistryTestCellContents {
    public static final BigInteger AMOUNT =
            RegistryBigIntegerCellLimits.MAXIMUM_PER_KEY;

    private static final int DATA_VERSION = 2;
    private static final String UUID_TAG = "infinity_cell_uuid";
    private static final String VERSION_TAG = "registry_long_test_cell_version";
    private static final String ITEM_COUNT_TAG = "registry_long_test_cell_item_keys";
    private static final String FLUID_COUNT_TAG = "registry_long_test_cell_fluid_keys";
    private static final String CHEMICAL_COUNT_TAG = "registry_long_test_cell_chemical_keys";

    private RegistryTestCellContents() {
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

        if (!force
                && tag.getInt(VERSION_TAG) == DATA_VERSION
                && manager.hasUUID(uuid)) {
            return readSummary(stack).orElseGet(
                    () -> rebuild(stack, manager, uuid));
        }
        return rebuild(stack, manager, uuid);
    }

    private static InitializationResult rebuild(
            ItemStack stack,
            InfinityStorageManager manager,
            UUID uuid) {
        Map<AEKey, BigInteger> amounts = new LinkedHashMap<>();

        int itemKeys = addItems(amounts);
        int fluidKeys = addFluids(amounts);
        int chemicalKeys = addChemicals(amounts);

        InfinityDataStorage storage = new InfinityDataStorage();
        storage.amounts.putAll(amounts);
        storage.itemCount = AMOUNT.multiply(BigInteger.valueOf(amounts.size()));
        manager.updateCell(uuid, storage);

        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(VERSION_TAG, DATA_VERSION);
        tag.putInt(ITEM_COUNT_TAG, itemKeys);
        tag.putInt(FLUID_COUNT_TAG, fluidKeys);
        tag.putInt(CHEMICAL_COUNT_TAG, chemicalKeys);

        // ExtendedAE Plus自身のセル表示にも、正確な合計と種類数を反映する。
        byte[] total = storage.itemCount.toByteArray();
        tag.putByteArray("infinity_item_total", total);
        tag.putByteArray("infinity_cell_item_count", total);
        tag.putInt("infinity_item_types", amounts.size());

        return new InitializationResult(itemKeys, fluidKeys, chemicalKeys);
    }

    private static int addItems(Map<AEKey, BigInteger> amounts) {
        int before = amounts.size();
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item == Items.AIR) {
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
        for (Fluid fluid : ForgeRegistries.FLUIDS.getValues()) {
            if (fluid == Fluids.EMPTY) {
                continue;
            }
            AEFluidKey key = AEFluidKey.of(fluid);
            if (key != null) {
                amounts.put(key, AMOUNT);
            }
        }
        return amounts.size() - before;
    }

    private static int addChemicals(Map<AEKey, BigInteger> amounts) {
        int before = amounts.size();
        addChemicalRegistry(amounts, MekanismAPI.gasRegistry());
        addChemicalRegistry(amounts, MekanismAPI.infuseTypeRegistry());
        addChemicalRegistry(amounts, MekanismAPI.pigmentRegistry());
        addChemicalRegistry(amounts, MekanismAPI.slurryRegistry());
        return amounts.size() - before;
    }

    private static <CHEMICAL extends Chemical<CHEMICAL>> void addChemicalRegistry(
            Map<AEKey, BigInteger> amounts,
            IForgeRegistry<CHEMICAL> registry) {
        for (CHEMICAL chemical : registry.getValues()) {
            if (chemical.isEmptyType()) {
                continue;
            }
            MekanismKey key = MekanismKey.of(chemical.getStack(1L));
            if (key != null) {
                amounts.put(key, AMOUNT);
            }
        }
    }

    public static Optional<InitializationResult> readSummary(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || tag.getInt(VERSION_TAG) != DATA_VERSION) {
            return Optional.empty();
        }
        return Optional.of(new InitializationResult(
                tag.getInt(ITEM_COUNT_TAG),
                tag.getInt(FLUID_COUNT_TAG),
                tag.getInt(CHEMICAL_COUNT_TAG)));
    }

    public record InitializationResult(
            int itemKeys,
            int fluidKeys,
            int chemicalKeys) {
    }
}
