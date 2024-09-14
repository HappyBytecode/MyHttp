package com.sesxh.smoothhttp.entity;

/**
 * @author LYH
 * @date 2021/1/13
 * @time 10:02
 * @desc 填充服务器返回的null。
 */

public class NullDataValue {
    private int defInt;
    private String defString;
    private float defFloat;
    private double defDouble;
    private long defLong;
    private boolean defBoolean;


    public NullDataValue defInt(int defInt) {
        this.defInt = defInt;
        return this;
    }

    public NullDataValue defString(String defString) {
        this.defString = defString;
        return this;
    }

    public NullDataValue defFloat(float defFloat) {
        this.defFloat = defFloat;
        return this;
    }

    public NullDataValue defDouble(double defDouble) {
        this.defDouble = defDouble;
        return this;
    }

    public NullDataValue defLong(long defLong) {
        this.defLong = defLong;
        return this;
    }

    public NullDataValue defBoolean(boolean defBoolean) {
        this.defBoolean = defBoolean;
        return this;
    }

    public int getDefInt() {
        return defInt;
    }

    public String getDefString() {
        return defString;
    }

    public float getDefFloat() {
        return defFloat;
    }

    public double getDefDouble() {
        return defDouble;
    }

    public long getDefLong() {
        return defLong;
    }

    public boolean getDefBoolean() {
        return defBoolean;
    }
}


