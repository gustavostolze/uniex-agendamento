package com.uniex.agendamento.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;

    @Column(name = "client_phone", nullable = false, length = 20)
    private String clientPhone;

    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private User professional;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ScheduleStatus status;

    @Column(name = "google_event_id", length = 250)
    private String googleEventId;

    public Schedule() {
    }

    public Schedule(Long id, String clientName, String clientPhone, User professional, Service service, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, ScheduleStatus status, String googleEventId) {
        this.id = id;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.professional = professional;
        this.service = service;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.status = status;
        this.googleEventId = googleEventId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public User getProfessional() {
        return professional;
    }

    public void setProfessional(User professional) {
        this.professional = professional;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public String getGoogleEventId() {
        return googleEventId;
    }

    public void setGoogleEventId(String googleEventId) {
        this.googleEventId = googleEventId;
    }
}
