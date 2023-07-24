package me.redtea.nodropx.libs.carcadex.reload;

public interface Reloadable {
    void init();

    void close();

    default void reload() {
        close();
        init();
    }

    default int priority() {
        return 0;
    }
}
