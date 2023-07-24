package me.redtea.nodropx.libs.message.factory;

import me.redtea.nodropx.libs.message.factory.impl.MessageFactoryImpl;
import me.redtea.nodropx.libs.message.model.Message;

import java.util.List;

import static me.redtea.nodropx.libs.message.container.Messages.NULL_MESSAGE;

public interface MessageFactory {
    Message message(List<String> unparsed);
    Message message(String msg);
    Message legacyMessage(List<String> unparsed);
    Message legacyMessage(String msg);

    default Message nullMessage() {
        return NULL_MESSAGE;
    }

    MessageFactory instance = new MessageFactoryImpl();
}
