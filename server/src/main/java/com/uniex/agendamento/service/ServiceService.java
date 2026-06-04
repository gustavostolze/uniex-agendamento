package com.uniex.agendamento.service;

import com.uniex.agendamento.model.Service;
import com.uniex.agendamento.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> getServicesByProfessional(Long professionalId) {
        return serviceRepository.findByProfessionalId(professionalId);
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado com o ID: " + id));
    }

    public Service saveService(Service service) {
        return serviceRepository.save(service);
    }

    public void deleteService(Long id) {
        Service service = getServiceById(id);
        serviceRepository.delete(service);
    }
}
