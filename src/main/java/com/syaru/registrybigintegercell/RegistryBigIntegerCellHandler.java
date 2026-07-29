package com.syaru.registrybigintegercell;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import com.extendedae_plus.items.InfinityBigIntegerCellItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.ServerLifecycleHooks;

/** Handles this mod's cells before ExtendedAE Plus' generic infinity-cell handler. */
public final class RegistryBigIntegerCellHandler implements ICellHandler {
    public static final RegistryBigIntegerCellHandler INSTANCE =
            new RegistryBigIntegerCellHandler();

    private RegistryBigIntegerCellHandler() {
    }

    @Override
    public boolean isCell(ItemStack stack) {
        return stack.getItem() instanceof RegistryBigIntegerTestCellItem
                || stack.getItem() instanceof ConfiguredBigIntegerTestCellItem;
    }

    @Override
    public StorageCell getCellInventory(
            ItemStack stack,
            ISaveProvider saveProvider) {
        // AE2は全セルハンドラへ順番に問い合わせるため、このMOD以外のセルは次のハンドラへ渡す。
        if (!isCell(stack)) {
            return null;
        }

        // 登録クラスの変更時にも不正キャストでワールドを停止させないため、実型を再確認する。
        InfinityBigIntegerCellItem cell =
                CellHandlerTypeGuard.castOrNull(
                        stack.getItem(),
                        InfinityBigIntegerCellItem.class);
        if (cell == null) {
            return null;
        }

        initializeOnServer(stack);
        return new LimitedBigIntegerCellInventory(
                cell,
                stack,
                saveProvider);
    }

    private static void initializeOnServer(ItemStack stack) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }
        if (stack.getItem() instanceof RegistryBigIntegerTestCellItem) {
            RegistryTestCellContents.ensureInitialized(stack, server, false);
        } else if (stack.getItem() instanceof ConfiguredBigIntegerTestCellItem) {
            ConfiguredTestCellContents.ensureInitialized(stack, server, false);
        }
    }
}
