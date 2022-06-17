package ru.itis.kpfu.mockdataserver.entity.server;

public class PluginResponse {

    private String endpoint;
    private String userId;
    private String nameOfRootModel;
    private String rootModel;
    private String additionalModels;
    private Boolean isStatic;
    private String locale;
    private Boolean isRepresentative;

    private Boolean isList;

    private int elementsCount;

    public Boolean getIsList() {
        return isList;
    }

    public void setList(Boolean isList) {
        this.isList = isList;
    }

    public int getElementsCount() {
        return elementsCount;
    }

    public void setElementsCount(int elementsCount) {
        this.elementsCount = elementsCount;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRootModel() {
        return rootModel;
    }

    public void setRootModel(String rootModel) {
        this.rootModel = rootModel;
    }

    public String getAdditionalModels() {
        return additionalModels;
    }

    public void setAdditionalModels(String additionalModels) {
        this.additionalModels = additionalModels;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsStatic() {
        return isStatic;
    }

    public void setIsStatic(Boolean isStatic) {
        this.isStatic = isStatic;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getNameOfRootModel() {
        return nameOfRootModel;
    }

    public void setNameOfRootModel(String nameOfRootModel) {
        this.nameOfRootModel = nameOfRootModel;
    }

    public Boolean getIsRepresentative() {
        return isRepresentative;
    }

    public void setIsRepresentative(Boolean isRepresentative) {
        this.isRepresentative = isRepresentative;
    }
}
