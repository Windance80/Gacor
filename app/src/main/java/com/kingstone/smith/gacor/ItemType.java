package com.kingstone.smith.gacor;

public abstract class ItemType {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_LIST = 1;

    abstract public int getType();
}
