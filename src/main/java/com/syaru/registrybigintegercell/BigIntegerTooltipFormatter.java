package com.syaru.registrybigintegercell;

import java.math.BigInteger;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/** ExtendedAE Plusの巨大なTotal行を、このMODの指数表記へ置き換える。 */
final class BigIntegerTooltipFormatter {
    private static final String PRIMARY_TOTAL_TAG = "infinity_item_total";
    private static final String LEGACY_TOTAL_TAG = "infinity_cell_item_count";
    private static final String TOTAL_PREFIX = "Total:";

    private BigIntegerTooltipFormatter() {
    }

    static void replaceParentTotal(
            ItemStack stack,
            List<Component> tooltip,
            int parentStartIndex) {
        BigInteger total = readTotal(stack);
        // 未初期化セルには合計NBTがないため、親ツールチップを変更しない。
        if (total == null) {
            return;
        }

        // super呼出し後に増えた行だけを調べ、このMODが後から追加する行には触れない。
        for (int index = parentStartIndex; index < tooltip.size(); index++) {
            Component line = tooltip.get(index);
            // ExtendedAE Plus 1.5.5が固定文字列で追加するTotal行だけを置換する。
            if (!line.getString().startsWith(TOTAL_PREFIX)) {
                continue;
            }
            tooltip.set(
                    index,
                    Component.literal(TOTAL_PREFIX + " ")
                            .withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(
                                            BigIntegerDisplayFormatter.format(total))
                                    .withStyle(ChatFormatting.AQUA)));
            return;
        }
    }

    private static BigInteger readTotal(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        // セルNBT自体がない場合は未初期化として扱う。
        if (tag == null) {
            return null;
        }

        String totalTag;
        // 現行ExtendedAE Plusのキーを優先し、旧キーは移行済みセル向けに残す。
        if (tag.contains(PRIMARY_TOTAL_TAG, Tag.TAG_BYTE_ARRAY)) {
            totalTag = PRIMARY_TOTAL_TAG;
        } else if (tag.contains(LEGACY_TOTAL_TAG, Tag.TAG_BYTE_ARRAY)) {
            totalTag = LEGACY_TOTAL_TAG;
        } else {
            return null;
        }

        byte[] encoded = tag.getByteArray(totalTag);
        // 空配列はBigIntegerコンストラクタが拒否するため、破損NBTとして親表示を維持する。
        if (encoded.length == 0) {
            return null;
        }
        return new BigInteger(encoded);
    }
}
