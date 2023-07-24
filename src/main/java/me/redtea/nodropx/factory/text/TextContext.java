package me.redtea.nodropx.factory.text;

import lombok.Data;
import me.redtea.nodropx.model.cosmetic.CosmeticStrategy;

import java.io.File;

@Data
public class TextContext {
    private final CosmeticStrategy loreStrategy;
    private final File messages;
    private final File cosmetics;
    private final boolean supportHex;
    private final boolean supportKyori;
}