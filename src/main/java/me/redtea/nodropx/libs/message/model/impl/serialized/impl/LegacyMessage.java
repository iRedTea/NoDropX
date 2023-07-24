package me.redtea.nodropx.libs.message.model.impl.serialized.impl;

import me.redtea.nodropx.libs.message.model.impl.serialized.SerializedMessage;
import me.redtea.nodropx.util.MD5ColorUtils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class LegacyMessage extends SerializedMessage {
    public LegacyMessage(List<String> unparsed) {
        super(unparsed.stream().map(MD5ColorUtils::translateHexColorCodes).collect(Collectors.toList()),
                LegacyComponentSerializer.builder().build());
    }
}
