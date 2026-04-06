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

Think of a container like a shipping container on a cargo ship — it packages everything needed for the app in one box, and it can run the same way on any machine.

---

## ✅ Prerequisites

Before you start, make sure you have:

- [ ] **Docker Desktop** installed and running on your machine
  - Download: [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
- [ ] A terminal / command prompt open (PowerShell, Terminal, Git Bash, etc.)

> **How to check:** Open a terminal and run `docker --version`. If you see a version number, you're good to go.

---

## 🚀 Option A — Clone the Project and Build the Image Yourself

This option puts you in the shoes of a **developer** — you'll download the source code and use Docker to build and run the application yourself, just like you would if you joined a team.

### Step 1 — Clone the Repository

```bash
git clone https://github.com/Christin-Classrooms/Week11-12-Tekken-Reborn.git
cd Week11-12-Tekken-Reborn
```

> **What's happening?**
> `git clone` downloads a full copy of the project from GitHub onto your computer. The `cd` command then moves you into that project folder so future commands run from the right place.

---

### Step 2 — Build the Docker Images

```bash
docker compose build
```

> **What's happening?**
> Docker reads the `Dockerfile` files inside the project and uses them as a recipe to build **images** — think of an image as a snapshot of the app and everything it needs (Java runtime, compiled code, etc.). This step compiles the Spring Boot apps inside Docker.
>
> ☕ This may take **3–5 minutes** the first time — it's downloading Java and resolving all the project dependencies. Subsequent builds are much faster because of caching.

---

### Step 3 — Start the Application

```bash
docker compose up -d
```

> **What's happening?**
> This command takes the images you just built and starts them as running **containers**. The `-d` flag stands for **detached**, meaning the containers run in the background and you get your terminal back.
>
> Docker Compose will start 3 containers in the right order:
> 1. **`mysql-db`** — the database (starts first, others wait for it to be healthy)
> 2. **`main-app`** — the main TekkenReborn web app on port `8080`
> 3. **`battle-service-app`** — the battle microservice on port `8081`

---

### Step 4 — Access the App

Open your browser and go to:

```
http://localhost:8080
```

> **What's happening?**
> Your browser is connecting to port `8080` on your machine, which Docker has mapped to the `main-app` container. The app is fully running inside Docker — you don't need Java installed locally at all.

---

## 🐋 Option B — Pull from Docker Hub (No Code Required)

This option puts you in the shoes of an **end user** — you'll run the application using pre-built images published to Docker Hub, without ever touching the source code. This is how most production deployments work in real life.

The images are publicly available here:

| Service | Docker Hub Link |
|---|---|
| Main App (port 8080) | [christinhumber/week9-10-main-app](https://hub.docker.com/r/christinhumber/week9-10-main-app) |
| Battle Service (port 8081) | [christinhumber/week9-10-battle-service](https://hub.docker.com/r/christinhumber/week9-10-battle-service) |

### Step 1 — Create a `docker-compose.yml` file

Create a new **empty folder** anywhere on your computer, then create a file called `docker-compose.yml` inside it with the following content:

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

> **What's happening?**
> This file is a **blueprint** that tells Docker what services to run, what images to use, what ports to expose, and how the containers should talk to each other. Notice there's no source code — Docker will download the images from Docker Hub automatically in the next step.

---

### Step 2 — Pull the Images

```bash
docker compose pull
```

> **What's happening?**
> This command downloads the pre-built images from **Docker Hub** (Docker's public image registry, similar to GitHub but for Docker images). After this step, everything the app needs is on your machine — no cloning, no compiling.

---

### Step 3 — Start Everything

```bash
docker compose up -d
```

> **What's happening?**
> Same as Option A Step 3 — Docker starts the 3 containers (database, main app, battle service) in the correct order in the background.

---

### Step 4 — Access the App

```
http://localhost:8080
```

---

## 🎮 Using the Application

Once the app is running:

1. Go to `http://localhost:8080` — you'll be redirected to the login page
2. **Register** a new account (you'll automatically be assigned the `PLAYER` role)
3. Or log in with the pre-seeded **Admin** account:
   - **Username:** `admin`
   - **Password:** `admin123`
4. Navigate to the **Battle** page from the navbar
5. Select two fighters from the dropdowns and click **FIGHT!**
6. You should see a battle result page showing the winner

---

## 📸 Required Screenshots for Submission

You must submit **4 screenshots**. Please name them clearly.

| # | What to capture | Suggested filename |
|---|---|---|
| 1 | Docker Desktop **or** `docker ps` output in your terminal — showing all 3 containers running (`mysql-db`, `main-app`, `battle-service-app`) | `screenshot-1-containers-running.png` |
| 2 | The app open in your browser at `http://localhost:8080` — showing the login or home page | `screenshot-2-app-running.png` |
| 3 | The **battle result page** — after selecting two fighters and clicking FIGHT | `screenshot-3-battle-result.png` |
| 4 | `docker ps` output showing **no containers running** after cleanup (see next section) | `screenshot-4-containers-stopped.png` |

---

## 🛑 Shutting Down and Cleaning Up

When you're done taking your screenshots, shut everything down properly.

### Stop and remove all containers

```bash
docker compose down
```

> **What's happening?**
> This stops all running containers and removes them. The images are still on your machine (so you could start again quickly), but the containers are gone.

### Optional — also delete the stored database

```bash
docker compose down -v
```

> **What's happening?**
> The `-v` flag also removes the **volume** — the stored database data. This gives you a completely clean slate, as if you never ran the app.

### Verify everything is stopped

```bash
docker ps
```

> **What's happening?**
> `docker ps` lists all **currently running** containers. After shutting down, this list should be empty. Take **Screenshot 4** here.

---

## 📤 Submission

Submit your 4 screenshots on **Blackboard** under the Optional Docker Lab assignment.

> **No pull request is needed for this lab** — just the screenshots are sufficient.

---

## 🆘 Troubleshooting

| Problem | Solution |
|---|---|
| `docker: command not found` | Docker Desktop is not installed or not running — open Docker Desktop first |
| Port 8080 already in use | Another app is using port 8080. Stop it, or change `"8080:8080"` to `"8082:8080"` in the compose file |
| App shows a database error on first load | Wait 20–30 seconds and refresh — MySQL takes a moment to fully initialize |
| Login doesn't work after registering | Make sure you're using the exact username and password you registered with |
| Containers stop immediately | Run `docker compose logs` to see error details |
| Battle says "service unavailable" | Wait another 30 seconds — the battle-service may still be starting up |
