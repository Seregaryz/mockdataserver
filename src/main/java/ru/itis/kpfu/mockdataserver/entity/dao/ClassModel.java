package ru.itis.kpfu.mockdataserver.entity.dao;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "class_model")
public class ClassModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean isRoot;

    private Boolean hasPrimitive;

    private Boolean hasInternal;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classModel")
    private List<PrimitiveField> primitiveFields;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classModel")
    private List<InternalField> internalFields;

    @ManyToOne
    @JoinColumn(name = "endpoint_id")
    private Endpoint endpoint;

    public ClassModel() {
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

    public Boolean getRoot() {
        return isRoot;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
    }

    public Boolean getHasPrimitive() {
        return hasPrimitive;
    }

    public void setHasPrimitive(Boolean hasPrimitive) {
        this.hasPrimitive = hasPrimitive;
    }

    public Boolean getHasInternal() {
        return hasInternal;
    }

    public void setHasInternal(Boolean hasInternal) {
        this.hasInternal = hasInternal;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public List<PrimitiveField> getPrimitiveFields() {
        return primitiveFields;
    }

    public void setPrimitiveFields(List<PrimitiveField> primitiveFields) {
        this.primitiveFields = primitiveFields;
    }

    public List<InternalField> getInternalFields() {
        return internalFields;
    }

    public void setInternalFields(List<InternalField> internalFields) {
        this.internalFields = internalFields;
    }
}
