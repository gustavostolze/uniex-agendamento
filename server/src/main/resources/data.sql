-- POPULANDO A TABELA DE USUÁRIOS (Apenas Profissionais agora na V1)

INSERT INTO users (name, email, telefone, type, google_access_token, google_refresh_token, google_calendar_id)
VALUES ('Tiago Cortador de Giro', 'tiagobarbeiro.udl@gmail.com', '34999991111', 'PROFISSIONAL', 'ya29.a0AfH6SMA...', '1//04G...', 'primary');

INSERT INTO work_days (professional_id, day_of_week, start_time, end_time) VALUES (1, 'MONDAY', '09:00:00', '18:00:00');
INSERT INTO work_days (professional_id, day_of_week, start_time, end_time) VALUES (1, 'TUESDAY', '09:00:00', '18:00:00');
INSERT INTO work_days (professional_id, day_of_week, start_time, end_time) VALUES (1, 'WEDNESDAY', '09:00:00', '18:00:00');
INSERT INTO work_days (professional_id, day_of_week, start_time, end_time) VALUES (1, 'THURSDAY', '09:00:00', '18:00:00');
INSERT INTO work_days (professional_id, day_of_week, start_time, end_time) VALUES (1, 'FRIDAY', '09:00:00', '18:00:00');

-- Serviços prestados

INSERT INTO service (professional_id, name, description, price, time_minutes)
VALUES (1, 'Corte Degradê Navalhado', 'Corte moderno com acabamento na navalha.', 40.00, 30);

INSERT INTO service (professional_id, name, description, price, time_minutes)
VALUES (1, 'Barba com Toalha Quente', 'Alinhamento de barba usando produtos premium.', 30.00, 45);

-- agendamentos

INSERT INTO schedules (client_name, client_phone, professional_id, service_id, data_hora_inicio, data_hora_fim, status, google_event_id)
VALUES ('Lucas Alcantara', '34988882222', 1, 1, '2026-06-10 14:00:00', '2026-06-10 14:30:00', 'CONFIRMADO', 'gcal_event_abc123xyz');

-- Mateus agendou outro serviço
INSERT INTO schedules (client_name, client_phone, professional_id, service_id, data_hora_inicio, data_hora_fim, status, google_event_id)
VALUES ('Mateus Souza', '34977773333', 1, 2, '2026-06-12 16:00:00', '2026-06-12 16:45:00', 'CANCELADO', 'gcal_event_987qweert');