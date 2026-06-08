package com.uniex.agendamento.controller;

import com.uniex.agendamento.model.Schedule;
import com.uniex.agendamento.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // GET: http://localhost:8080/api/schedules/professional/1
    // O profissional usa para ver todos os agendamentos marcados para ele no Dashboard
    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<Schedule>> getSchedulesByProfessional(@PathVariable Long professionalId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByProfessional(professionalId));
    }

    // POST: http://localhost:8080/api/schedules
    // O cliente final chama essa rota quando preenche o formulário de Nome/Zap e clica em Confirmar
    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleRequestDTO request) {
        Schedule novoAgendamento = scheduleService.createSchedule(
                request.getClientName(),
                request.getClientPhone(),
                request.getProfessionalId(),
                request.getServiceId(),
                request.getDataHoraInicio()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAgendamento);
    }

    // GET: http://localhost:8080/api/schedules/horarios-livres?professionalId=1&serviceId=1&data=2026-06-15
    // O Front chama isso quando o cliente clica em um dia do calendário!
    @GetMapping("/horarios-livres")
    public ResponseEntity<List<String>> getHorariosLivres(
            @RequestParam Long professionalId,
            @RequestParam Long serviceId,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate data) {

        List<String> horarios = scheduleService.getHorariosDisponiveis(professionalId, serviceId, data);
        return ResponseEntity.ok(horarios);
    }
}