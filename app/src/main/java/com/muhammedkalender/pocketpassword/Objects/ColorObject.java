package com.muhammedkalender.pocketpassword.Objects;

public class ColorObject {
    private int color;
    private int tint;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTint() {
        return tint;
    }

    public void setTint(int tint) {
        this.tint = tint;
    }

    public ColorObject(int color, int tint) {
        this.color = color;
        this.tint = tint;
    }
}
