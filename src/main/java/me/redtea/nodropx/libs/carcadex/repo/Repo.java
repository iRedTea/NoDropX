package me.redtea.nodropx.libs.carcadex.repo;

import me.redtea.nodropx.libs.carcadex.reload.Reloadable;
import me.redtea.nodropx.libs.carcadex.repo.builder.RepoBuilder;

import java.util.Collection;
import java.util.Optional;

public interface Repo<K, V> extends Reloadable {
    Collection<V> all();

    Optional<V> get(K key);

    static<K, V> RepoBuilder<K, V> builder() {
        return RepoBuilder.get();
    }
}