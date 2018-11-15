package com.kingstone.smith.gacor.Gacor;

public class ItemHeatspot extends ItemType {
    private Gacor gacor;

    @Override
    public int getType() {
        return TYPE_HEATSPOT;
    }

    public ItemHeatspot(Gacor gacor) {
        this.gacor = gacor;
    }

    public Gacor getGacor() {
        return gacor;
    }
}
