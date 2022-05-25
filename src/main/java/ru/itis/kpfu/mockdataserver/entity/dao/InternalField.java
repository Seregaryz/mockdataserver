package ru.itis.kpfu.mockdataserver.entity.dao;

import javax.persistence.*;

@Entity
@Table(name = "internal_field")
public class InternalField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type_name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ClassModel classModel;

    public InternalField() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public ClassModel getClassModel() {
        return classModel;
    }

    public void setClassModel(ClassModel classModel) {
        this.classModel = classModel;
    }
}
