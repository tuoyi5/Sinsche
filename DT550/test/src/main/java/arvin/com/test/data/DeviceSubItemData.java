package arvin.com.test.data;

/**
 * Created by tuoyi on 2017/9/13 0013.
 */

public class DeviceSubItemData {


    private String subItemDataCode;
    private String subItemDataName;
    private String subItemDataValue;
    private String subItemDataUnit;
    private boolean bWaterState = true;
    private boolean bOverLevel = true;

    private boolean subItemDataVisibility = true;

    public String subItemDataTestTime;

    public DeviceSubItemData() {

    }

    public DeviceSubItemData(String subItemDataCode,
                             String subItemDataName,
                             String subItemDataValue,
                             String subItemDataUnit,
                             boolean bWaterState,
                             boolean bOverLevel) {
        this.subItemDataCode = subItemDataCode;
        this.subItemDataName = subItemDataName;
        this.subItemDataValue = subItemDataValue;
        this.subItemDataUnit = subItemDataUnit;
        this.bWaterState = bWaterState;
        this.bOverLevel = bOverLevel;

    }

    public String getSubItemDataCode() {
        return subItemDataCode;
    }

    public void setSubItemDataCode(String subItemDataCode) {
        this.subItemDataCode = subItemDataCode;
    }

    public String getSubItemDataName() {
        return subItemDataName;
    }

    public void setSubItemDataName(String subItemDataName) {
        this.subItemDataName = subItemDataName;
    }

    public String getSubItemDataValue() {
        return subItemDataValue;
    }

    public void setSubItemDataValue(String subItemDataValue) {
        this.subItemDataValue = subItemDataValue;
    }

    public String getSubItemDataUnit() {
        return subItemDataUnit;
    }

    public void setSubItemDataUnit(String subItemDataUnit) {
        this.subItemDataUnit = subItemDataUnit;
    }

    public boolean isSubItemDataVisibility() {
        return subItemDataVisibility;
    }

    public void setSubItemDataVisibility(boolean subItemDataVisibility) {
        this.subItemDataVisibility = subItemDataVisibility;
    }

    public boolean isbWaterState() {
        return bWaterState;
    }

    public void setbWaterState(boolean bWaterState) {
        this.bWaterState = bWaterState;
    }

    public boolean isbOverLevel() {
        return bOverLevel;
    }

    public void setbOverLevel(boolean bOverLevel) {
        this.bOverLevel = bOverLevel;
    }

    public String getSubItemDataTestTime() {
        return subItemDataTestTime;
    }

    public void setSubItemDataTestTime(String subItemDataTestTime) {
        this.subItemDataTestTime = subItemDataTestTime;
    }
}
