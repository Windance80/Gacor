package com.kingstone.smith.gacor.Gacor;

import com.kingstone.smith.gacor.ItemType;

import java.util.List;

public class GacorList extends ItemType {

    private Gacor gacor;

    public GacorList(Gacor gacor) {
        this.gacor = gacor;
    }

    public Gacor getGacor() {
        return gacor;
    }

    @Override
    public int getType() {
        return TYPE_LIST;
    }
}
