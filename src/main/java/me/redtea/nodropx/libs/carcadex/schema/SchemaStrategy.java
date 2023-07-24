package me.redtea.nodropx.libs.carcadex.schema;

import me.redtea.nodropx.libs.carcadex.reload.Reloadable;

import java.util.Collection;

public interface SchemaStrategy<K, V> extends Reloadable {
    Collection<V> all();

    V get(K key);

    void insert(K key, V value);

    void remove(K key);
}
