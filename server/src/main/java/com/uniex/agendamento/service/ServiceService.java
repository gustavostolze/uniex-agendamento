package com.uniex.agendamento.service;

import com.uniex.agendamento.model.Service;
import com.uniex.agendamento.model.User;
import com.uniex.agendamento.repository.ServiceRepository;
import com.uniex.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Service> getServicesByProfessional(Long professionalId) {
        return serviceRepository.findByProfessionalId(professionalId);
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado com o ID: " + id));
    }

    public Service saveService(Long professionalId, String name, String description, Double price, Integer timeMinutes) {
        User professional = userRepository.findById(professionalId)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado no sistema."));

        Service novoServico = new Service();
        novoServico.setName(name);
        novoServico.setDescription(description);
        novoServico.setPrice(BigDecimal.valueOf(price));
        novoServico.setTimeMinutes(timeMinutes);

        novoServico.setProfessional(professional);

        return serviceRepository.save(novoServico);
    }

    public Service updateService(Long id, String name, String description, Double price, Integer timeMinutes) {
        Service existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para atualização."));

        existingService.setName(name);
        existingService.setDescription(description);
        existingService.setPrice(BigDecimal.valueOf(price));
        existingService.setTimeMinutes(timeMinutes);

        return serviceRepository.save(existingService);
    }

    public void deleteService(Long id) {
        Service service = getServiceById(id);
        serviceRepository.delete(service);
    }
}
