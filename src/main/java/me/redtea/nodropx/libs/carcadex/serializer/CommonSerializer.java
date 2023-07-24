package me.redtea.nodropx.libs.carcadex.serializer;

public interface CommonSerializer<T> {
    String serialize(T t);
    T deserialize(String s);
}
