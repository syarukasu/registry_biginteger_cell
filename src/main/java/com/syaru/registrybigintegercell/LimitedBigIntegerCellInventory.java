package com.syaru.registrybigintegercell;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.storage.cells.ISaveProvider;
import com.extendedae_plus.ExtendedAEPlus;
import com.extendedae_plus.api.storage.InfinityBigIntegerCellInventory;
import com.extendedae_plus.items.InfinityBigIntegerCellItem;
import com.extendedae_plus.util.storage.InfinityDataStorage;
import com.extendedae_plus.util.storage.InfinityStorageManager;
import com.syaru.ae2craftingoptimizer.api.vector.ExactVectorStoragePolicy;
import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/** ExtendedAE Plus inventory with a strict per-key stock ceiling. */
public final class LimitedBigIntegerCellInventory
        extends InfinityBigIntegerCellInventory
        implements ExactVectorStoragePolicy {
    private static final String UUID_TAG = "infinity_cell_uuid";
    private final ItemStack stack;

    public LimitedBigIntegerCellInventory(
            InfinityBigIntegerCellItem cell,
            ItemStack stack,
            ISaveProvider saveProvider) {
        super(cell, stack, saveProvider);
        this.stack = stack;
    }

    @Override
    public long insert(
            AEKey key,
            long amount,
            Actionable mode,
            IActionSource source) {
        if (amount <= 0L || !allows(key)) {
            return 0L;
        }

        BigInteger current = currentAmount(key);
        BigInteger remaining =
                RegistryBigIntegerCellLimits.remainingCapacity(current);
        if (remaining.signum() == 0) {
            return 0L;
        }

        long accepted = remaining.compareTo(BigInteger.valueOf(amount)) < 0
                ? remaining.longValueExact()
                : amount;
        return super.insert(key, accepted, mode, source);
    }

    @Override
    public BigInteger acoMaximumExactInsert(
            AEKey key,
            BigInteger currentAmount) {
        BigInteger current = Objects.requireNonNull(
                currentAmount, "currentAmount");
        // Config対象外キー、負数、上限到達済みキーへACOの直接挿入を許可しない。
        if (!allows(Objects.requireNonNull(key, "key"))
                || current.signum() < 0) {
            return BigInteger.ZERO;
        }
        return RegistryBigIntegerCellLimits.remainingCapacity(current);
    }

    private boolean allows(AEKey key) {
        return !(stack.getItem() instanceof ConfiguredBigIntegerTestCellItem)
                || RegistryBigIntegerCellConfig.allows(key);
    }

    private BigInteger currentAmount(AEKey key) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.hasUUID(UUID_TAG)) {
            return BigInteger.ZERO;
        }

        InfinityStorageManager manager = ExtendedAEPlus.STORAGE_INSTANCE;
        if (manager == null) {
            return RegistryBigIntegerCellLimits.MAXIMUM_PER_KEY;
        }

        UUID uuid = tag.getUUID(UUID_TAG);
        InfinityDataStorage storage = manager.getOrCreateCell(uuid);
        return storage.amounts.getOrDefault(key, BigInteger.ZERO);
    }
}
