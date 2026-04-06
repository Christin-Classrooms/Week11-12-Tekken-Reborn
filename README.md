# TekkenReborn — 🐳 Optional Docker Lab

## Course Information

* **Course:** CPAN 228

---

> **⭐ Grade Incentive:**
> This is an **extra optional lab**. If you complete and submit it, **your lowest graded lab will be dropped** from your final grade. If you skip it, don't worry — this lab itself will be treated as your lowest grade and will be automatically dropped anyway. Either way, you're covered!

This lab introduces you to **Docker** — a tool used in almost every real-world software team to package and run applications in a consistent, portable environment. Instead of writing code, you will focus on **running** a pre-built application using Docker and documenting the process with screenshots.

---

## 🧱 What is Docker? (Quick Intro)

Docker lets you run applications inside **containers** — lightweight, isolated environments that include everything the app needs to run (Java, the database, dependencies, etc.). You don't need to install MySQL or Java on your machine; Docker handles all of that for you.

---

## ✅ Prerequisites

Before you start, make sure you have:

- [ ] **Docker Desktop** installed and running on your machine
  - Download: [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
- [ ] A terminal / command prompt open (PowerShell, Terminal, Git Bash, etc.)

> **How to check:** Open a terminal and run `docker --version`. If you see a version number, you're good to go.

---

## 🚀 Option A — Clone the Project and Build the Image Yourself

This option shows you how a developer would run the full project locally using Docker.

### Step 1 — Clone the Repository

```bash
git clone https://github.com/Christin-Classrooms/Week9-10-Spring-Security-Lab.git
cd Week9-10-Spring-Security-Lab
```

### Step 2 — Build the Docker Images

This command reads the `Dockerfile` in each module and builds the images:

```bash
docker compose build
```

> ☕ This may take a few minutes the first time — it's downloading Java and all the project dependencies.

### Step 3 — Start the Application

```bash
docker compose up -d
```

The `-d` flag means **detached** — it runs in the background.

### Step 4 — Access the App

Open your browser and go to:

```
http://localhost:8080
```

---

## 🐋 Option B — Pull from Docker Hub (No Code Required)

This option shows you how a user would run an app they've never seen before, using only a public Docker image.

### Step 1 — Create a `docker-compose.yml` file

Create a new folder anywhere on your computer, then create a file called `docker-compose.yml` inside it with this content:

```yaml
services:
  db:
    image: mysql:8.0
    container_name: mysql-db
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: fighters_db
    ports:
      - "3307:3306"
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-proot"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    image: christinhumber/week9-10-main-app:latest
    container_name: main-app
    ports:
      - "8080:8080"
    depends_on:
      db:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/fighters_db?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      BATTLE_SERVICE_URL: http://battle-service:8081/battles

  battle-service:
    image: christinhumber/week9-10-battle-service:latest
    container_name: battle-service-app
    ports:
      - "8081:8081"
    depends_on:
      db:
        condition: service_healthy
      app:
        condition: service_started
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/fighters_db?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      FIGHTER_SERVICE_URL: http://app:8080/api/fighters
```

### Step 2 — Pull and Start Everything

In the same folder as your `docker-compose.yml`, run:

```bash
docker compose pull
docker compose up -d
```

### Step 3 — Access the App

```
http://localhost:8080
```

---

## 🎮 Using the Application

Once the app is running:

1. Go to `http://localhost:8080`
2. You will be redirected to the login page
3. Register a new account (you'll be assigned the `PLAYER` role)
4. Or log in with the admin account:
   - **Username:** `admin`
   - **Password:** `admin123`
5. Navigate to the **Battle** page and select two fighters to fight

---

## 📸 Required Screenshots for Submission

You must submit **4 screenshots**. Please name them clearly.

| # | What to capture | Suggested filename |
|---|---|---|
| 1 | Docker Desktop (or `docker ps` output in terminal) showing all 3 containers running (`mysql-db`, `main-app`, `battle-service-app`) | `screenshot-1-containers-running.png` |
| 2 | The app open in your browser at `http://localhost:8080` — showing the login or home page | `screenshot-2-app-running.png` |
| 3 | The **battle result page** — after selecting two fighters and clicking FIGHT | `screenshot-3-battle-result.png` |
| 4 | After stopping and deleting the containers — `docker ps` output showing **no containers running** | `screenshot-4-containers-stopped.png` |

---

## 🛑 Shutting Down and Cleaning Up

When you're done, stop and remove all containers:

```bash
docker compose down
```

To also remove the stored database data (volume):

```bash
docker compose down -v
```

After running this, verify that the containers are gone:

```bash
docker ps
```

You should see an empty list — take **Screenshot 4** now.

---

## 📤 Submission

Submit your 4 screenshots on **Blackboard** under the Optional Docker Lab assignment.

> **No pull request is needed for this lab** — just the screenshots are sufficient.

---

## 🆘 Troubleshooting

| Problem | Solution |
|---|---|
| `docker: command not found` | Docker Desktop is not installed or not started — open Docker Desktop first |
| Port 8080 already in use | Another app is using port 8080. Stop it, or change `"8080:8080"` to `"8082:8080"` in the compose file |
| App shows a database error | Wait 30 seconds and refresh — MySQL takes a moment to initialize |
| Login doesn't work after registering | Make sure you're using the exact username and password you registered with |
| Containers stop immediately | Run `docker compose logs` to see error details |
