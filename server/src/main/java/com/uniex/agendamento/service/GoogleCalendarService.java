package com.uniex.agendamento.service;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.OAuth2Credentials;
import com.uniex.agendamento.model.Schedule;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
public class GoogleCalendarService {

    // Instancia o formatador de JSON padrão do SDK do Google
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public String criarEventoNoCalendario(Schedule schedule) {
        try {
            // 1. Inicializa o meio de transporte HTTP exigido pelo SDK do Google
            final NetHttpTransport httpTransport = new NetHttpTransport();

            // 2. Recupera o token de acesso que salvamos no profissional do agendamento
            String accessTokenStr = schedule.getProfessional().getGoogleAccessToken();

            if (accessTokenStr == null) {
                System.out.println("⚠️ Profissional sem token do Google cadastrado. Pulando inserção no Calendar.");
                return null;
            }

            // 3. Monta as credenciais que a nova versão do SDK exige
            AccessToken accessToken = new AccessToken(accessTokenStr, null);
            OAuth2Credentials credentials = OAuth2Credentials.create(accessToken);
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

            // 4. Constrói o cliente do Google Calendar
            Calendar client = new Calendar.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                    .setApplicationName("Agendamento MEI")
                    .build();

            // 5. Cria a estrutura do Evento
            Event event = new Event()
                    .setSummary(schedule.getService().getName() + " - " + schedule.getClientName())
                    .setDescription("Cliente: " + schedule.getClientName() + "\nTelefone: " + schedule.getClientPhone());

            // 6. Converte o LocalDateTime do Java para o formato do Google com o fuso horário local
            ZoneId fusoHorario = ZoneId.systemDefault(); // Pega o fuso da máquina (ex: America/Sao_Paulo)

            Date dataInicio = Date.from(schedule.getDataHoraInicio().atZone(fusoHorario).toInstant());
            EventDateTime start = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(dataInicio))
                    .setTimeZone(fusoHorario.getId());
            event.setStart(start);

            Date dataFim = Date.from(schedule.getDataHoraFim().atZone(fusoHorario).toInstant());
            EventDateTime end = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(dataFim))
                    .setTimeZone(fusoHorario.getId());
            event.setEnd(end);

            // 7. Envia o comando para o Google inserir na agenda principal do profissional
            String calendarId = schedule.getProfessional().getGoogleCalendarId(); // Padrão: "primary"
            Event eventCriado = client.events().insert(calendarId, event).execute();

            System.out.println("✅ Evento sincronizado no Google Calendar com ID: " + eventCriado.getId());

            // Retorna o ID do evento gerado pelo Google para salvarmos no nosso banco local
            return eventCriado.getId();

        } catch (Exception e) {
            System.err.println("❌ Erro ao integrar com o Google Calendar: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}