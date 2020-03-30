package com.kalendersoftware.pocketpassword.Objects;

public class ColumnObject {
    private String name, type, def;
    private boolean primary, notNull, auto_increment;
    private int minLength, maxLength;

    public ColumnObject(){

    }

    public ColumnObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ColumnObject setName(String name) {
        this.name = name;

        return this;
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

    public ColumnObject setDef(){
        if(getType() == "INTEGER"){
            return setDef("0");
        }else{
            return setDef("");
        }
    }

    public ColumnObject setDef(String def) {
        this.def = def;

        return this;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public boolean isAutoIncrement() {
        return auto_increment;
    }

    public ColumnObject setNotNull(){
        return this.setNotNull(true);
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

    public ColumnObject setAutoIncrement(){
        setAutoIncrement(true);

        return this;
    }

    public ColumnObject setAutoIncrement(boolean set){
        this.auto_increment = set;

        return this;
    }
}
