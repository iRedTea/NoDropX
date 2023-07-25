package me.redtea.nodropx.libs.message.container.impl;

import lombok.Setter;
import me.redtea.nodropx.libs.message.verifier.MessageVerifier;
import me.redtea.nodropx.libs.message.container.Messages;
import me.redtea.nodropx.libs.message.factory.MessageFactory;
import me.redtea.nodropx.libs.message.model.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class MessagesImpl implements Messages {
    private Map<String, Message> messages;

    private MessageFactory factory = MessageFactory.instance;

    private final Message NULL_MESSAGE = factory.nullMessage();

    private ConfigurationSection section;

    @Setter
    private MessageVerifier verifier;

    public MessagesImpl() {
    }

    public MessagesImpl(@NotNull ConfigurationSection section) {
        this.section = section;
    }
    public MessagesImpl(@NotNull ConfigurationSection section, MessageVerifier verifier) {
        this.section = section;
        this.verifier = verifier;
    }

    public MessagesImpl(@NotNull File file) {
        this(YamlConfiguration.loadConfiguration(file));
    }

    public MessagesImpl(@NotNull File file, MessageVerifier verifier) {
        this(YamlConfiguration.loadConfiguration(file));
        this.verifier = verifier;
    }



    @Override
    public Message get(String key) {
        if(!has(key)) {
            if(verifier != null) {
                Optional<String> def = verifier.fromDefault(key);
                if(def.isPresent()) {
                    messages.put(key, factory.message(def.get()));
                    return messages.get(key);
                }
                return NULL_MESSAGE;
            }
            return NULL_MESSAGE;
        }
        return messages.get(key) == null ? NULL_MESSAGE : messages.get(key);
    }

    @Override
    public Message put(String key, Message message) {
        return messages.put(key, message);
    }

    @Override
    public boolean has(String key) {
        return messages.containsKey(key);
    }

    @Override
    public void reload(ConfigurationSection section) {
        messages = fromConfigurationToMap(section);
    }

    @Override
    public Messages factory(MessageFactory messageFactory) {
        this.factory = messageFactory;
        return this;
    }

    @Override
    public Messages init() {
        messages = fromConfigurationToMap(section);
        return this;
    }

    private Map<String, Message> fromConfigurationToMap(@NotNull ConfigurationSection section) {
        Map<String, Message> data = new HashMap<>();

        section.getKeys(false).forEach(key -> {
            if (section.isConfigurationSection(key)) {
                Map<String, Message> newMessages = fromConfigurationToMap(Objects.
                        requireNonNull(section.getConfigurationSection(key)));
                newMessages.forEach((keyMessage, message) -> data.put(key + "." + keyMessage, message));
            } else {
                Message message = null;
                if (section.isString(key)) {
                    message = factory.message(section.getString(key));
                } else if (section.isList(key)) {
                    message = factory.message(section.getStringList(key));
                }

                if (message != null) {
                    data.put(key, message);
                }
            }
        });

        return data;
    }


}
