# Sistema de Agendamento Escalável (MVP)

Projeto de expansão focado em microempreendedores (MEI) com integração ao Google Calendar.

## 🛠️ Tecnologias
- **Frontend:** React + TypeScript (Vite)
- **Backend:** Java + Spring Boot
- **Banco de Dados:** PostgreSQL

## 🚀 Como Rodar o Frontend (Client)
1. Certifique-se de ter o [Node.js](https://nodejs.org/) instalado.
2. No terminal, navegue até a pasta: `cd client`
3. Instale as dependências: `npm install`
4. Inicie o servidor de desenvolvimento: `npm run dev`

## ☕ Como Rodar o Backend (Server)
1. Certifique-se de ter o [JDK 21](https://adoptium.net/en-GB/temurin/releases/?version=21) instalado.
2. No terminal, navegue até a pasta: `cd server`
3. Rode a aplicação com o Maven Wrapper:
   - No Linux/macOS: `./mvnw spring-boot:run`
   - No Windows: `mvnw.cmd spring-boot:run`
3. Ou rode a aplicação com o Maven da sua máquina:
   - Comando: `mvn spring-boot:run`
4. A API estará rodando em `http://localhost:8080`
5. O console do banco de dados pode ser acessado em `http://localhost:8080/h2-console` (User: `sa`, Password: em branco)