package ru.rksp.samylovskikh.processor.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Сырые события университета (таблица raw_university_events / сырые_события_университета).
 */
@Entity
@Table(name = "raw_university_events")
public class RawUniversityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "fio_prepodavatelya", nullable = false)
    private String fioPrepodavatelya;

    @Column(nullable = false)
    private String disciplina;

    @Column(nullable = false)
    private String auditoriya;

    @Column(name = "data_sobytiya", nullable = false)
    private Instant dataSobytiya;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getFioPrepodavatelya() {
        return fioPrepodavatelya;
    }

    public void setFioPrepodavatelya(String fioPrepodavatelya) {
        this.fioPrepodavatelya = fioPrepodavatelya;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getAuditoriya() {
        return auditoriya;
    }

    public void setAuditoriya(String auditoriya) {
        this.auditoriya = auditoriya;
    }

    public Instant getDataSobytiya() {
        return dataSobytiya;
    }

    public void setDataSobytiya(Instant dataSobytiya) {
        this.dataSobytiya = dataSobytiya;
    }
}
