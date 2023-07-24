package me.redtea.nodropx.libs.carcadex.repo;

import me.redtea.nodropx.libs.carcadex.reload.Reloadable;

public interface MutableRepo<K, V> extends Repo<K, V>, Reloadable {

    V update(K key, V value);

    V remove(K key);

    void saveAll();
}
