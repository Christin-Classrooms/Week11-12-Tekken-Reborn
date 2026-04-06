# Battle Service - Deployment Guide

Follow these steps to run the microservices architecture locally using Docker.

## Step 1: Build the Main Application
Open a terminal in the root project folder (where `Dockerfile` for Main-App is) and run:
```bash
docker build -t main-app:latest .
```

## Step 2: Launch Orchestration
Now that the main app image exists, start all services (MySQL, Main-App, Battle-Service) from the root folder:
```bash
docker-compose up --build
```

---

## Testing the Microservice

### 1. Simulate a Fight
Use `curl` or Postman to send a POST request to the battle service:
```bash
curl -X POST http://localhost:8081/battles \
  -H "Content-Type: application/json" \
  -d '{"fighter1Id":1,"fighter2Id":2,"playerId":1,"playerName":"Alice"}'
```

### 2. View Battle History
To see all recorded battles:
```bash
curl http://localhost:8081/battles
```

### 3. View Player History
To see matches for a specific player:
```bash
curl http://localhost:8081/battles/player/1
```
