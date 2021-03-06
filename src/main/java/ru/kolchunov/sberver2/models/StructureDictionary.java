package ru.kolchunov.sberver2.models;

import javax.persistence.*;

/**
 * Entity for structure_dictionary_table
 */
@Entity
@Table(name = "structure_dictionary_table")
public class StructureDictionary {
    /**
     * Id field
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    /**
     * Id dictionary
     */
    @Basic
    @Column(name = "id_dictionary", nullable = false)
    private Long idDictionary;
    @Basic
    /**
     * Name field
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    /**
     * Type data of the field
     */
    @Basic
    @Column(name = "data_type", nullable = false)
    private DataTypes dataType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDictionary() {
        return idDictionary;
    }

    public void setIdDictionary(Long idDictionary) {
        this.idDictionary = idDictionary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataTypes getDataType() {
        return dataType;
    }

    public void setDataType(DataTypes dataType) {
        this.dataType = dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StructureDictionary that = (StructureDictionary) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idDictionary != null ? !idDictionary.equals(that.idDictionary) : that.idDictionary != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (dataType != null ? !dataType.equals(that.dataType) : that.dataType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idDictionary != null ? idDictionary.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        return result;
    }
}
