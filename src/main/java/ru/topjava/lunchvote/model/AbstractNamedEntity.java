package ru.topjava.lunchvote.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
public class AbstractNamedEntity extends AbstractEntity {

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 3, max = 100)
    protected String name;

    public AbstractNamedEntity() {
    }

    public AbstractNamedEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AbstractNamedEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
