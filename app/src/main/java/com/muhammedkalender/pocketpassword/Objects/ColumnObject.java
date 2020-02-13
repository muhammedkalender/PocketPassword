package com.muhammedkalender.pocketpassword.Objects;

public class ColumnObject {
    private String name, type, def;
    private boolean notNull;
    private int minLength, maxLength;

    public ColumnObject() {
    }

    public ColumnObject(String name, String type, String def, boolean notNull, int maxLength) {
        this.name = name;
        this.type = type;
        this.def = def;
        this.notNull = notNull;
        this.maxLength = maxLength;
    }

    public ColumnObject(String name, String type, String def, boolean notNull, int minLength, int maxLength) {
        this.name = name;
        this.type = type;
        this.def = def;
        this.notNull = notNull;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }
}
