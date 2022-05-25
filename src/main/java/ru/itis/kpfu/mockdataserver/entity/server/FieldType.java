package ru.itis.kpfu.mockdataserver.entity.server;

public class FieldType {

    private String type;

    private Boolean isPrimitive;

    public FieldType() {
    }

    public FieldType(String type, Boolean isPrimitive) {
        this.type = type;
        this.isPrimitive = isPrimitive;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPrimitive() {
        return isPrimitive;
    }

    public void setPrimitive(Boolean primitive) {
        isPrimitive = primitive;
    }
}
