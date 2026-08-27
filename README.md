# Patient Service

## Student Information
* **Name:** Lahiru Mudith
* **Student Number:** CMS-ST-2026
* **GCP Project ID:** `cms-cloud-architecture`

---

## Service Overview
The **Patient Service** handles patient information, validation checks, and profile picture storage on Google Cloud. It utilizes PostgreSQL for metadata records and Google Cloud Storage (GCS) for hosting profile binary files.

* **Default Port:** `8000`
* **Data Store:** PostgreSQL (Database: `cms_patient`)
* **Storage Provider:** Google Cloud Storage (GCS bucket).
* **Target Environment:** OpenJDK 25, Spring Boot 4.1.1, Spring Cloud 2025.1.3.

---

## Validation Rules & Schema Validation

### Sri Lankan NIC Validation:
The service validates both old and new formats:
* **Old NIC Pattern:** `^\d{9}[vVxX]$` (9 digits followed by letters V/X case-insensitive).
* **New NIC Pattern:** `^\d{12}$` (Exactly 12 digits).

### Blood Group Pattern:
* Matches: `A+`, `A-`, `B+`, `B-`, `O+`, `O-`, `AB+`, `AB-`.

---

## REST Endpoints (Routed via Gateway Port `7000`)

| Method | Endpoint | Description | Request Format |
|---|---|---|---|
| **POST** | `/api/v1/patients` | Register new patient | `multipart/form-data` (includes image file) |
| **GET** | `/api/v1/patients` | List all patient details | JSON Array |
| **GET** | `/api/v1/patients/{patientId}` | Get single patient record | JSON |
| **PUT** | `/api/v1/patients/{patientId}` | Modify patient profile data | `multipart/form-data` |
| **DELETE** | `/api/v1/patients/{patientId}` | Remove patient from registry | None |
| **GET** | `/api/v1/patients/{patientId}/photo` | Retrieve patient avatar stream | JPEG Binary stream |

---

## Build and Run

### Build Command:
```bash
mvn clean package -DskipTests
```

### Execution Command:
```bash
java -jar target/Patient-Service-1.0.0.jar
```

---

## 🧪 Testing with Postman
A dedicated testing collection is available locally:
👉 **[Patient-Service-Postman-Collection.json](file:///C:/Users/Lahiru%20Mudith/Documents/Study/IJSE/4th%20Sem/Enterprise%20Cloud%20Architectur/Projects/Clinic-Management-System/cms-services/service-patient/Patient-Service-Postman-Collection.json)**

* **Usage:** Import the collection file into Postman.
* **Environment Configuration:** Adjust the collection-scoped `baseUrl` variable to point to `http://localhost:8000` (for direct testing) or `http://localhost:7000` (when testing via the gateway).
