package ru.itis.kpfu.mockdataserver.entity.dao.representative;

import javax.persistence.*;

@Entity
@Table(name = "verb")
public class Verb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
