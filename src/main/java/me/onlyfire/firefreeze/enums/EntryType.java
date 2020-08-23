package me.onlyfire.firefreeze.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EntryType {
    FREEZE("Freeze"), UNFREEZE("Unfreeze"), QUIT("Quit"), FORCED("Forced");

    private String s;

    public String toName(){
        return s;
    }
}
