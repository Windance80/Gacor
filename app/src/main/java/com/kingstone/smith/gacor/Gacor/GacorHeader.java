package com.kingstone.smith.gacor.Gacor;

import com.kingstone.smith.gacor.ItemType;

public class GacorHeader extends ItemType {
    private int type;

    public GacorHeader(int type) {
        this.type = type;
    }

    public String getHeader() {
        switch (type) {
            case 0: {
                return "PLACE";
            }
            case 1: {
                return "TIME";
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}
