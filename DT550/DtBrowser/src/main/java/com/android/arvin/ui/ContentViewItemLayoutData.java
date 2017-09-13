package com.android.arvin.ui;

/**
 * Created by tuoyi on 2017/9/10 0010.
 */

public  class ContentViewItemLayoutData {

    private int gridRowCount;
    private int gridDefaultShowRowCount;
    private int gridColumnCount;
    private int itemhight;
    private int itemSize;

    public ContentViewItemLayoutData(){

    }

    public int getGridRowCount() {
        return gridRowCount;
    }

    public void setGridRowCount(int gridRowCount) {
        this.gridRowCount = gridRowCount;
    }

    public int getGridDefaultShowRowCount() {
        return gridDefaultShowRowCount;
    }

    public void setGridDefaultShowRowCount(int gridDefaultShowRowCount) {
        this.gridDefaultShowRowCount = gridDefaultShowRowCount;
    }

    public int getGridColumnCount() {
        return gridColumnCount;
    }

    public void setGridColumnCount(int gridColumnCount) {
        this.gridColumnCount = gridColumnCount;
    }

    public int getItemhight() {
        return itemhight;
    }

    public void setItemhight(int itemhight) {
        this.itemhight = itemhight;
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }
}