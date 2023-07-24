package me.redtea.nodropx.libs.message.factory.impl;

import com.google.common.collect.Lists;
import me.redtea.nodropx.libs.message.factory.MessageFactory;
import me.redtea.nodropx.libs.message.model.Message;
import me.redtea.nodropx.libs.message.model.impl.ComponentMessage;
import me.redtea.nodropx.libs.message.model.impl.serialized.impl.LegacyMessage;

import java.util.List;

public class MessageFactoryImpl implements MessageFactory {
    @Override
    public Message message(List<String> unparsed) {
        if(unparsed == null || unparsed.isEmpty()) return nullMessage();
        return new ComponentMessage(unparsed);
    }

    @Override
    public Message message(String msg) {
        return message(Lists.newArrayList(msg));
    }

    @Override
    public Message legacyMessage(List<String> unparsed) {
        if(unparsed == null || unparsed.isEmpty()) return nullMessage();
        return new LegacyMessage(unparsed);
    }

    @Override
    public Message legacyMessage(String msg) {
        return legacyMessage(Lists.newArrayList(msg));
    }
}
