package com.uniex.agendamento.repository;

import com.uniex.agendamento.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByProfessionalId(Long professionalId);
}