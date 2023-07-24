package me.redtea.nodropx.libs.carcadex.repo.impl.common;

import me.redtea.nodropx.libs.carcadex.repo.impl.schema.SchemaRepo;
import me.redtea.nodropx.libs.carcadex.schema.impl.binary.BinarySchemaStrategy;
import me.redtea.nodropx.libs.carcadex.schema.impl.serialize.SerializeSchemaStrategy;
import me.redtea.nodropx.libs.carcadex.serializer.CommonSerializer;

import java.nio.file.Path;

public class CommonRepo<K, V> extends SchemaRepo<K, V> {
    public CommonRepo(Path dir, CommonSerializer<V> serializer) {
        super(new SerializeSchemaStrategy<>(dir, serializer));
    }

    public CommonRepo(Path dir) {
        super(new BinarySchemaStrategy<>(dir));
    }
}
