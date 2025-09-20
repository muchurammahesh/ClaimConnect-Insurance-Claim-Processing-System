# 🏥 ClaimConnect – Insurance Claim Processing System

An **end-to-end insurance claim platform** that reduces claim approval time by 30% through **automated workflows** using **Spring Boot Microservices** and **React (Vite)**.  
The system provides **role-based dashboards** for hospitals, patients, and insurance companies, ensuring efficiency and transparency.

---

<img width="1865" height="899" alt="claimconnect" src="https://github.com/user-attachments/assets/1d24bf8c-f22e-4de6-9f45-a214eb9d4b71" />

## 🚀 Key Features

- ✅ Microservices Architecture for scalability and modularity  
- ✅ Secure API Gateway for authentication and routing  
- ✅ Eureka Server for service discovery and fault tolerance  
- ✅ Centralized Config Server (GitHub-integrated) for configuration management  
- ✅ React + Vite frontend with Redux for fast, modern UI  
- ✅ Role-Based Access Control (RBAC) for hospitals, patients, and insurers  
- ✅ JWT-based secure authentication  
- ✅ Optimized data storage with MySQL  
- ✅ Deployable with Docker and Kubernetes  

---

## 🏗 Architecture Overview

1. **Client (React + Vite Frontend)**  
   - Users submit and track insurance claims via a responsive UI.  
   - Frontend communicates with backend through the API Gateway.  

2. **API Gateway (Spring Cloud Gateway)**  
   - Handles request routing and authentication.  
   - Provides load balancing.  

3. **Eureka Server (Service Discovery)**  
   - Registers all microservices dynamically.  
   - Enables service-to-service communication.  

4. **Config Server (Spring Cloud Config)**  
   - Provides centralized configuration.  
   - Fetches configs from a GitHub repository for version control.  

5. **Microservices**  
   - 🏥 **Hospital Service** – Processes hospital records & treatments  
   - 🧑‍⚕️ **Patient Service** – Manages patient data  
   - 🏢 **Insurance Service** – Handles policy verification & claim settlements  
   - 📄 **Claim Request Service** – Submits, approves, and processes claims  

---

## 🛠 Tech Stack

- **Frontend:** React (Vite), Redux, Bootstrap  
- **Backend:** Spring Boot, Spring Cloud (Eureka, Config Server, Gateway)  
- **Database:** MySQL  
- **Service Communication:** REST APIs, Feign Client  
- **Security:** Spring Security, JWT Authentication  
- **Deployment:** Docker, Kubernetes  

---

## 📂 Folder Structure

ClaimConnect/
├── frontend/ # React + Vite application
│ ├── public/
│ ├── src/
│ │ ├── components/
│ │ ├── pages/
│ │ ├── redux/
│ │ └── App.jsx
│ ├── index.html
│ ├── package.json
│ └── vite.config.js
├── backend/ # Spring Boot microservices
│ ├── claim-service/
│ ├── hospital-service/
│ ├── patient-service/
│ ├── insurance-service/
│ ├── gateway-service/
│ ├── config-server/
│ └── eureka-server/
└── README.md

yaml
Copy code

---

## ⚙️ Setup & Installation

### 1. Clone the Repository
```bash
git clone https://github.com/muchurammahesh/ClaimConnect.git
cd ClaimConnect
2. Backend Setup (Spring Boot Microservices)
Import each microservice into your IDE (IntelliJ/Eclipse).

Configure application.properties with MySQL credentials.

Start services in order:

Config Server

Eureka Server

API Gateway

Other microservices (Hospital, Patient, Insurance, Claim Request)

3. Frontend Setup (React + Vite)
bash
Copy code
cd frontend
npm install
npm run dev
Open the app at:
👉 http://localhost:5173


👨‍💻 Author
Muchuram Mahesh
📌 GitHub Profile
