package com.syaru.registrybigintegercell;

import com.extendedae_plus.items.InfinityBigIntegerCellItem;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/** Common configで指定したItem/Fluidだけをsigned long超過量保持するセル。 */
public final class ConfiguredBigIntegerTestCellItem
        extends InfinityBigIntegerCellItem {
    public ConfiguredBigIntegerTestCellItem() {
        super(new Item.Properties());
    }

    @Override
    public void inventoryTick(
            ItemStack stack,
            Level level,
            Entity entity,
            int slotId,
            boolean selected) {
        super.inventoryTick(stack, level, entity, slotId, selected);
        if (level instanceof ServerLevel serverLevel) {
            ConfiguredTestCellContents.ensureInitialized(
                    stack,
                    serverLevel.getServer(),
                    false);
        }
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        if (level instanceof ServerLevel serverLevel) {
            ConfiguredTestCellContents.ensureInitialized(
                    stack,
                    serverLevel.getServer(),
                    false);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            ConfiguredTestCellContents.InitializationResult result =
                    ConfiguredTestCellContents.ensureInitialized(
                            stack,
                            serverLevel.getServer(),
                            player.isShiftKeyDown());
            player.displayClientMessage(
                    Component.translatable(
                            player.isShiftKeyDown()
                                    ? "message.registry_biginteger_cell.configured_refreshed"
                                    : "message.registry_biginteger_cell.configured_ready",
                            result.itemKeys(),
                            result.fluidKeys(),
                            ConfiguredTestCellContents.AMOUNT.toString()),
                    true);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Level level,
            List<Component> tooltip,
            TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable(
                        "tooltip.registry_biginteger_cell.configured_amount",
                        ConfiguredTestCellContents.AMOUNT.toString())
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        ConfiguredTestCellContents.readSummary(stack).ifPresentOrElse(
                summary -> tooltip.add(Component.translatable(
                                "tooltip.registry_biginteger_cell.configured_contents",
                                summary.itemKeys(),
                                summary.fluidKeys())
                        .withStyle(ChatFormatting.AQUA)),
                () -> tooltip.add(Component.translatable(
                                "tooltip.registry_biginteger_cell.uninitialized")
                        .withStyle(ChatFormatting.YELLOW)));
        tooltip.add(Component.translatable("tooltip.registry_biginteger_cell.refresh")
                .withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
