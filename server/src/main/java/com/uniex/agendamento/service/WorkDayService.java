package com.uniex.agendamento.service;

import com.uniex.agendamento.model.WorkDay;
import com.uniex.agendamento.model.User;
import com.uniex.agendamento.repository.WorkDayRepository;
import com.uniex.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkDayService {

    @Autowired
    private WorkDayRepository workDayRepository;

    @Autowired
    private UserRepository userRepository;

    public List<WorkDay> getDisponibilidade(Long professionalId) {
        return workDayRepository.findByProfessionalId(professionalId);
    }

    @Transactional
    public List<WorkDay> salvarDisponibilidade(Long professionalId, List<WorkDay> novosDias) {
        User professional = userRepository.findById(professionalId)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        // Limpa os dias antigos cadastrados para não duplicar na tabela
        List<WorkDay> diasAntigos = workDayRepository.findByProfessionalId(professionalId);
        workDayRepository.deleteAll(diasAntigos);

        // Vincula cada dia novo ao profissional logado e salva
        novosDias.forEach(dia -> dia.setProfessional(professional));
        return workDayRepository.saveAll(novosDias);
    }
}