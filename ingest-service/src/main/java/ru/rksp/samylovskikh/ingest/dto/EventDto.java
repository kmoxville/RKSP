package ru.rksp.samylovskikh.ingest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Schema(description = "Событие университета")
public class EventDto {

    @Schema(description = "Идентификатор")
    private String id;

    @NotBlank
    @Schema(description = "ФИО преподавателя", required = true)
    @JsonProperty("fio_prepodavatelya")
    private String fioPrepodavatelya;

    @NotBlank
    @Schema(description = "Дисциплина", required = true)
    private String disciplina;

    @NotBlank
    @Schema(description = "Аудитория", required = true)
    private String auditoriya;

    @NotNull
    @Schema(description = "Дата события", required = true)
    @JsonProperty("data_sobytiya")
    private Instant dataSobytiya;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
