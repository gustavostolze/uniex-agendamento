-- Usuário 1: O Profissional (MEI)
INSERT INTO users (id, name, email, telefone, type, google_access_token, google_refresh_token, google_calendar_id)
VALUES (1, 'Tiago Cortador de Giro', 'tiagobarbeiro.udl@gmail.com', '34999991111', 'PROFISSIONAL', 'ya29.a0AfH6SMA...', '1//04G...', 'primary');

-- Usuário 2: Cliente A
INSERT INTO users (id, name, email, telefone, type, google_access_token, google_refresh_token, google_calendar_id)
VALUES (2, 'Lucas Alcantara', 'lucas.cliente@gmail.com', '34988882222', 'CLIENTE', NULL, NULL, NULL);

-- Usuário 3: Cliente B
INSERT INTO users (id, name, email, telefone, type, google_access_token, google_refresh_token, google_calendar_id)
VALUES (3, 'Mateus Souza', 'mateus.silva@outlook.com', '34977773333', 'CLIENTE', NULL, NULL, NULL);


-- Serviço 1: Corte de Cabelo (R$ 40,00 - 30 minutos)
INSERT INTO service (id, professional_id, name, description, price, time_minutes)
VALUES (1, 1, 'Corte Degradê Navalhado', 'Corte moderno com acabamento na navalha e lavagem inclusa.', 40.00, 30);

-- Serviço 2: Barba Completa (R$ 30,00 - 45 minutos)
INSERT INTO service (id, professional_id, name, description, price, time_minutes)
VALUES (2, 1, 'Barba com Toalha Quente', 'Alinhamento de barba usando produtos premium e técnica de relaxamento com toalha quente.', 30.00, 45);


-- ====================================================================
-- 3. POPULANDO A TABELA DE AGENDAMENTOS (Tabela: schedule)
-- ====================================================================
-- Agendamento 1: Lucas agendou com Tiago para o dia 10/06 às 14:00 (Confirmado)
INSERT INTO schedule (id, client_id, professional_id, service_id, data_hora_inicio, data_hora_fim, status, google_event_id)
VALUES (1, 2, 1, 1, '2026-06-10 14:00:00', '2026-06-10 14:30:00', 'CONFIRMADO', 'gcal_event_abc123xyz');

-- Agendamento 2: Mateus tinha agendado uma Barba, mas cancelou
INSERT INTO schedule (id, client_id, professional_id, service_id, data_hora_inicio, data_hora_fim, status, google_event_id)
VALUES (2, 3, 1, 2, '2026-06-12 16:00:00', '2026-06-12 16:45:00', 'CANCELADO', 'gcal_event_987qweert');