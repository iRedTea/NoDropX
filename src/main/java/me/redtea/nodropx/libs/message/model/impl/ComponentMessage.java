package me.redtea.nodropx.libs.message.model.impl;

import me.redtea.nodropx.libs.message.model.AbstractMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class ComponentMessage extends AbstractMessage {
    private final MiniMessage mm = MiniMessage.get();

    public ComponentMessage(List<String> unparsed) {
        super(unparsed);
    }

    @Override
    protected Component parse(String unparsed) {
        return mm.deserialize(unparsed);
    }
}
