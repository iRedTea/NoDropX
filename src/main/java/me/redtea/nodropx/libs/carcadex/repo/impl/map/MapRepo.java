package me.redtea.nodropx.libs.carcadex.repo.impl.map;

import me.redtea.nodropx.libs.carcadex.reload.parameterized.ParameterizedReloadable;
import me.redtea.nodropx.libs.carcadex.reload.parameterized.container.ReloadContainer;
import me.redtea.nodropx.libs.carcadex.repo.Repo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Reload container:
 * data - Map.class
 * @param <K>
 * @param <V>
 */
public abstract class MapRepo<K, V> extends ParameterizedReloadable implements Repo<K, V> {
    protected Map<K, V> data = new HashMap<>();

    @Override
    public Collection<V> all() {
        return data.values();
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(data.get(key));
    }

    @Override
    public void init(ReloadContainer container) {
        if(container != null) data = container.get("data");
    }

    @Override
    public void close() {
        data.clear();
    }
}
