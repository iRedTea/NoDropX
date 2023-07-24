package me.redtea.nodropx.schema;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import me.redtea.nodropx.libs.carcadex.schema.SchemaStrategy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ItemStackListSchema implements SchemaStrategy<UUID, List<ItemStack>> {
    private final File dir;

    @Override
    public Collection<List<ItemStack>> all() {
        val res = new ArrayList<List<ItemStack>>();
        for(File file : dir.listFiles()) {
            res.add(get(UUID.fromString(file.getName().substring(file.getName().length()-4))));
        }
        return res;
    }

    @Override
    public List<ItemStack> get(UUID key) {
        File file = new File(dir, key.toString() + ".yml");
        if(!file.exists()) return new ArrayList<>();
        val conf = YamlConfiguration.loadConfiguration(file);
        val res = new ArrayList<ItemStack>();
        for(String k : conf.getKeys(false)) {
            res.add(conf.getItemStack(k));
        }
        return res;
    }

    @Override
    @SneakyThrows
    public void insert(UUID key, List<ItemStack> value) {
        File file = new File(dir, key.toString() + ".yml");
        if(!file.exists()) file.createNewFile();
        val conf = YamlConfiguration.loadConfiguration(file);
        for(int i = 0; i < value.size(); ++i) {
            conf.set(String.valueOf(i), value.get(i));
        }
        conf.save(file);
    }

    @Override
    public void remove(UUID key) {
        File file = new File(dir, key.toString() + ".yml");
        if(file.exists()) file.delete();
    }

    @Override
    @SneakyThrows
    public void init() {
        if(!dir.exists()) {
            Files.createDirectories(dir.toPath());
        }
    }

    @Override
    public void close() {

    }

}
