package com.uniex.agendamento.repository;

import com.uniex.agendamento.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByProfessionalId(Long professionalId);

    @Query("SELECT s FROM Schedule s WHERE s.professional.id = :professionalId " +
            "AND s.status = 'CONFIRMADO' " +
            "AND (:inicio < s.dataHoraFim AND :fim > s.dataHoraInicio)")
    List<Schedule> findOverlappingSchedules(
            @Param("professionalId") Long professionalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}