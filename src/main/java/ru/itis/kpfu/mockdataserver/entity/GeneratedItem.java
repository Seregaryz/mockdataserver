package ru.itis.kpfu.mockdataserver.entity;

public class GeneratedItem {

    private int typeId;
    private String generatedValue;

    public GeneratedItem(int typeId, String generatedValue) {
        this.typeId = typeId;
        this.generatedValue = generatedValue;
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
}
