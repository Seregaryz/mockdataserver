package ru.itis.kpfu.mockdataserver.entity.dao;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "endpoint")
public class Endpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userPath;

    private String additionalPath;

    private boolean isStatic;

    private String locale;

    private boolean isRepresentative;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "endpoint")
    private List<ClassModel> classModels;

    public Endpoint() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }

    public String getAdditionalPath() {
        return additionalPath;
    }

    public void setAdditionalPath(String additionalPath) {
        this.additionalPath = additionalPath;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public List<ClassModel> getClassModels() {
        return classModels;
    }

    public void setClassModels(List<ClassModel> classModels) {
        this.classModels = classModels;
    }

    public boolean getIsRepresentative() {
        return isRepresentative;
    }

    public void setIsRepresentative(boolean isRepresentative) {
        this.isRepresentative = isRepresentative;
    }
}
