package com.uniex.agendamento.controller;

import com.uniex.agendamento.model.WorkDay;
import com.uniex.agendamento.service.WorkDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-days")
public class WorkDayController {

    @Autowired
    private WorkDayService workDayService;

    // GET: http://localhost:8080/api/work-days/professional/2
    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<WorkDay>> getByProfessional(@PathVariable Long professionalId) {
        return ResponseEntity.ok(workDayService.getDisponibilidade(professionalId));
    }

    // POST: http://localhost:8080/api/work-days/professional/2
    // Envia o array de dias que o cara vai trabalhar
    @PostMapping("/professional/{professionalId}")
    public ResponseEntity<List<WorkDay>> salvar(@PathVariable Long professionalId, @RequestBody List<WorkDay> dias) {
        return ResponseEntity.ok(workDayService.salvarDisponibilidade(professionalId, dias));
    }
}