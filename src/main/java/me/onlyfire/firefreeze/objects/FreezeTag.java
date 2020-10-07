package me.onlyfire.firefreeze.objects;

import lombok.Getter;

@Getter
public class FreezeTag {
    private String prefix;
    private String suffix;

    public FreezeTag(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }
}
