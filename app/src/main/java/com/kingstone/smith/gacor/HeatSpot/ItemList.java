package com.kingstone.smith.gacor.HeatSpot;

import com.kingstone.smith.gacor.ItemType;

public class ItemList extends ItemType {

    private ModelData modelData;

    public ItemList(ModelData modelData) {
        this.modelData = modelData;
    }

    public ModelData getModelData() {
        return modelData;
    }

    @Override
    public int getType() {
        return TYPE_LIST;
    }
}
