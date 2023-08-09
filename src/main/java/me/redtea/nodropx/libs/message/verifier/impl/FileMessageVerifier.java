package me.redtea.nodropx.libs.message.verifier.impl;

import lombok.SneakyThrows;
import me.redtea.nodropx.libs.message.verifier.MessageVerifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

public class FileMessageVerifier implements MessageVerifier {
    private ConfigurationSection defaultSection;

    private final FileConfiguration toSaveSection;
    private final File toSaveFile;

    public FileMessageVerifier(InputStream defaultResource, FileConfiguration toSaveSection, File toSaveFile) {
        defaultSection = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultResource));
        this.toSaveSection = toSaveSection;
        this.toSaveFile = toSaveFile;
    }

    @Override
    public Optional<Object> fromDefault(String key) {
        Optional<Object> o = Optional.ofNullable(defaultSection.get(key));
        o.ifPresent(s -> saveToMain(key, s));
        return o;
    }

    @SneakyThrows
    public void saveToMain(String path, Object obj) {
        toSaveSection.set(path, obj);
        toSaveSection.save(toSaveFile);
    }
}
