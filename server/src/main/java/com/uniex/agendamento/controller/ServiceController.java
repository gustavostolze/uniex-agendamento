package com.uniex.agendamento.controller;

import com.uniex.agendamento.model.Service;
import com.uniex.agendamento.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<Service>> getServicesByProfessional(@PathVariable Long professionalId) {
        List<Service> services = serviceService.getServicesByProfessional(professionalId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody ServiceRequestDTO request) {
        Service novoServico = serviceService.saveService(
                request.getProfessionalId(),
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getTimeMinutes()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(novoServico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody ServiceRequestDTO request) {
        Service updatedService = serviceService.updateService(
                id,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getTimeMinutes()
        );
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}