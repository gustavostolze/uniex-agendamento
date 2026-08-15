package com.uniex.agendamento.controller;

public class ServiceRequestDTO {
    private Long professionalId;
    private String name;
    private String description;
    private Double price;
    private Integer timeMinutes;

    public Long getProfessionalId() {
        return professionalId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getTimeMinutes() {
        return timeMinutes;
    }
}