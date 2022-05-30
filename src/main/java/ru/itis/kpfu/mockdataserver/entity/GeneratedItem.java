package ru.itis.kpfu.mockdataserver.entity;

public class GeneratedItem {

    private int typeId;
    private String generatedValue;
    private boolean isList;

    public GeneratedItem(int typeId, String generatedValue, boolean isList) {
        this.typeId = typeId;
        this.generatedValue = generatedValue;
        this.isList = isList;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getGeneratedValue() {
        return generatedValue;
    }

    public void setGeneratedValue(String generatedValue) {
        this.generatedValue = generatedValue;
    }

    public boolean isList() {
        return isList;
    }

    public void setIsList(boolean list) {
        isList = list;
    }
}
