package com.muhammedkalender.pocketpassword.Objects;

public class ColumnObject {
    private String name, type, def;
    private boolean primary, notNull, auto_increment;
    private int minLength, maxLength;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public ColumnObject setType(String type) {
        this.type = type;

        return this;
    }

    public String getDef() {
        return def;
    }

    public ColumnObject setDef(String def) {
        this.def = def;

        return this;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public ColumnObject setNotNull(boolean notNull) {
        this.notNull = notNull;

        return this;
    }

    public int getMinLength() {
        return minLength;
    }

    public ColumnObject setMinLength(int minLength) {
        this.minLength = minLength;

        return this;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public ColumnObject setMaxLength(int maxLength) {
        this.maxLength = maxLength;

        return this;
    }

    public boolean isPrimary() {
        return primary;
    }

    public ColumnObject setPrimary(){
        this.setPrimary(true);

        return this;
    }

    public ColumnObject setPrimary(boolean set){
        this.primary = set;

        return this;
    }

    public void setAutoIncrement(){
        setAutoIncrement(true);
    }

    public ColumnObject setAutoIncrement(boolean set){
        this.auto_increment = set;

        return this;
    }
}
