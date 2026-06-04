package com.uniex.agendamento.repository;

import com.uniex.agendamento.model.WorkDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {
    List<WorkDay> findByProfessionalId(Long professionalId);
}