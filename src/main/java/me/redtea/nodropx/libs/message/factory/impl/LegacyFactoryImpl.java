package me.redtea.nodropx.libs.message.factory.impl;

import com.google.common.collect.Lists;
import me.redtea.nodropx.libs.message.factory.MessageFactory;
import me.redtea.nodropx.libs.message.model.Message;
import me.redtea.nodropx.libs.message.model.impl.serialized.impl.LegacyMessage;
import me.redtea.nodropx.util.MD5ColorUtils;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

public class LegacyFactoryImpl implements MessageFactory {
    @Override
    public Message message(List<String> unparsed) {
        if(unparsed == null || unparsed.isEmpty()) return nullMessage();
        if(unparsed.stream().anyMatch(it -> it.contains("&"))) unparsed =
                unparsed.stream()
                .map(MD5ColorUtils::translateHexColorCodes)
                .collect(Collectors.toList());
        return new LegacyMessage(unparsed);
    }

    @Override
    public Message message(String msg) {
        if(msg == null) return nullMessage();
        if(msg.contains("&")) msg = MD5ColorUtils.translateHexColorCodes(msg);
        return new LegacyMessage(Lists.newArrayList(msg));
    }

    @Override
    public Message legacyMessage(List<String> unparsed) {
        return message(unparsed);
    }

    @Override
    public Message legacyMessage(String msg) {
        return message(Lists.newArrayList(msg));
    }

}
