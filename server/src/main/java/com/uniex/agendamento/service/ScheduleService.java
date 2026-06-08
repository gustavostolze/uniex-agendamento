package com.uniex.agendamento.service;

import com.uniex.agendamento.model.*;
import com.uniex.agendamento.repository.ScheduleRepository;
import com.uniex.agendamento.repository.ServiceRepository;
import com.uniex.agendamento.repository.UserRepository;
import com.uniex.agendamento.repository.WorkDayRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@org.springframework.stereotype.Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private WorkDayRepository workDayRepository;

    // Listar todos os agendamentos de um profissional (Para o Dashboard dele)
    public List<Schedule> getSchedulesByProfessional(Long professionalId) {
        return scheduleRepository.findByProfessionalId(professionalId);
    }

    public Schedule createSchedule(String clientName, String clientPhone, Long professionalId, Long serviceId, LocalDateTime dataHoraInicio) {

        // 1. Busca e valida o profissional e o serviço no banco
        User professional = userRepository.findById(professionalId)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // 2. Calcula a data/hora de término automaticamente baseado na duração do serviço
        LocalDateTime dataHoraFim = dataHoraInicio.plusMinutes(service.getTimeMinutes());

        // 3. Verifica choque de horários na agenda do profissional
        List<Schedule> conflitos = scheduleRepository.findOverlappingSchedules(professionalId, dataHoraInicio, dataHoraFim);
        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Desculpe, este horário já está ocupado por outro cliente!");
        }

        // 4. Monta o objeto Schedule
        Schedule schedule = new Schedule();
        schedule.setClientName(clientName);
        schedule.setClientPhone(clientPhone);
        schedule.setProfessional(professional);
        schedule.setService(service);
        schedule.setDataHoraInicio(dataHoraInicio);
        schedule.setDataHoraFim(dataHoraFim);
        schedule.setStatus(ScheduleStatus.CONFIRMADO);

        // 5. ENVIA PARA O GOOGLE CALENDAR
        String googleEventId = googleCalendarService.criarEventoNoCalendario(schedule);

        // Seta o ID retornado pelo Google (pode ser null se a integração falhar, mas o agendamento local continua salvo)
        schedule.setGoogleEventId(googleEventId);

        // 6. Salva definitivamente no nosso banco H2 local
        return scheduleRepository.save(schedule);
    }

    public List<String> getHorariosDisponiveis(Long professionalId, Long serviceId, LocalDate data) {
        // 1. Busca qual é o dia da semana da data que o cliente escolheu (ex: MONDAY, TUESDAY...)
        String diaDaSemana = data.getDayOfWeek().toString();

        // 2. Busca no banco se o profissional trabalha nesse dia da semana
        List<WorkDay> disponibilidades = workDayRepository.findByProfessionalId(professionalId);
        WorkDay expediente = disponibilidades.stream()
                .filter(w -> w.getDayOfWeek().equalsIgnoreCase(diaDaSemana))
                .findFirst()
                .orElse(null);

        // Se ele não trabalha nesse dia (ex: domingo), retorna a lista vazia na hora!
        if (expediente == null) {
            return java.util.Collections.emptyList();
        }

        // 3. Busca o serviço para saber o tempo de duração dele (ex: 30 minutos)
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        int duracao = service.getTimeMinutes();

        // 4. Busca todos os agendamentos já ocupados que o profissional tem REAIS nesse dia específico
        List<Schedule> agendamentosDoDia = scheduleRepository.findByProfessionalId(professionalId).stream()
                .filter(s -> s.getStatus() == ScheduleStatus.CONFIRMADO && s.getDataHoraInicio().toLocalDate().equals(data))
                .toList();

        List<String> horariosLivres = new java.util.ArrayList<>();

        // 5. Laço que vai "andando" no tempo, do horário de início até o fim do expediente
        LocalTime horaAtual = expediente.getStartTime();
        LocalTime horaFimExpediente = expediente.getEndTime();

        while (horaAtual.plusMinutes(duracao).isBefore(horaFimExpediente) || horaAtual.plusMinutes(duracao).equals(horaFimExpediente)) {
            LocalTime fimDoBloco = horaAtual.plusMinutes(duracao);

            // Verifica se esse bloco de tempo tromba com algum agendamento do H2
            LocalTime finalHoraAtual = horaAtual;
            boolean estaOcupado = agendamentosDoDia.stream().anyMatch(s -> {
                LocalTime agendamentoInicio = s.getDataHoraInicio().toLocalTime();
                LocalTime agendamentoFim = s.getDataHoraFim().toLocalTime();
                // Verifica sobreposição de horário dentro do mesmo dia
                return (finalHoraAtual.isBefore(agendamentoFim) && fimDoBloco.isAfter(agendamentoInicio));
            });

            // Se o horário não tiver nenhum cliente, adiciona na lista de opções livres!
            if (!estaOcupado) {
                horariosLivres.add(horaAtual.toString()); // Adiciona o texto ex: "09:00"
            }

            // Avança o relógio de acordo com a duração do serviço para avaliar o próximo bloco!
            horaAtual = horaAtual.plusMinutes(duracao);
        }

        return horariosLivres;
    }
}