# Smart Clinic Management System - Architecture Documentation

## Section 1: Architecture Summary

The Smart Clinic Management System is a robust Spring Boot application engineered with a polyglot persistence architecture and a multi-layered design. To accommodate different client needs, the presentation layer utilizes a hybrid approach: it leverages the traditional MVC pattern via **Thymeleaf Controllers** to dynamically render server-side HTML dashboards for Administrators and Doctors, while concurrently employing **REST Controllers** to expose decoupled, JSON-based REST APIs for modules like Appointments, Patient Dashboards, and Patient Records.

At the core of the application is a unified **Service Layer**, which isolates the core business logic and orchestrates data movement between the controllers and the persistence layers. To efficiently handle diverse data requirements, the system splits data storage across two distinct database technologies:
* **MySQL Database:** Handles highly structured, relational data. It interacts with the application using the Java Persistence API (JPA) to manage core domain models like Patients, Doctors, Appointments, and Admins.
* **MongoDB Database:** Handles semi-structured or unstructured data. It utilizes document-based models specifically tailored to manage flexible data structures like medical Prescriptions.

---

## Section 2: Numbered flow of data and control

The following numbered sequence describes the complete end-to-end flow of data and execution control through the application layers, corresponding to the steps marked 1 through 7 on the architecture diagram:

1. **Client Interaction:** The end-user or client application initiates an action by either accessing the front-end UI (`AdminDashboard` / `DoctorDashboard`) or invoking service endpoints via client-side `RESTModules` (`Appointments`, `PatientDashboard`, or `PatientRecord`).
2. **Controller Routing:** The presentation layer routes the request into the application; UI dashboard requests are directed to the `Thymeleaf Controllers` for view rendering, whereas REST module interactions utilize JSON APIs to communicate directly with the `REST Controllers`.
3. **Service Layer Delegation:** After receiving and parsing the incoming requests, the controllers call the central `Service Layer` where the core business rules and transactional logic are executed.
4. **Repository Coordination:** The `Service Layer` determines the nature of the data operation and uses the appropriate data access layer, executing queries through either the `MySQL Repositories` (for structured entities) or the `MongoDB Repository` (for document datasets).
5. **Database Execution:** The repository components establish communication with and access their respective underlying database management systems—performing operations against either the relational `MySQL Database` or the document-based `MongoDB Database`.
6. **Data Object Mapping:** The persistence tier translates raw database records or documents into the application's runtime data mapping structures, categorized as either low-level `MySQL Models` or `MongoDB Models`.
7. **Entity / Document Resolution:** Finally, these models are resolved into strongly-typed Java domain objects defined within the codebase—represented either as object-relational `JPA Entities` (`Patient`, `Doctor`, `Appointment`, `Admin`) or as a flexible MongoDB `Document` (`Prescription`).

## MySQL Database Design

### Table: admin
* `id`: INT, Primary Key, Auto Increment
* `username`: VARCHAR(50), Unique, Not Null
* `password_hash`: VARCHAR(255), Not Null
* `email`: VARCHAR(100), Unique, Not Null

### Table: doctors
* `id`: INT, Primary Key, Auto Increment
* `first_name`: VARCHAR(50), Not Null
* `last_name`: VARCHAR(50), Not Null
* `email`: VARCHAR(100), Unique, Not Null
* `specialization`: VARCHAR(100), Not Null
* `phone`: VARCHAR(20)

### Table: patients
* `id`: INT, Primary Key, Auto Increment
* `first_name`: VARCHAR(50), Not Null
* `last_name`: VARCHAR(50), Not Null
* `email`: VARCHAR(100), Unique, Not Null
* `phone`: VARCHAR(20)
* `date_of_birth`: DATE, Not Null

### Table: appointments
* `id`: INT, Primary Key, Auto Increment
* `doctor_id`: INT, Foreign Key -> doctors(id)
* `patient_id`: INT, Foreign Key -> patients(id)
* `appointment_time`: DATETIME, Not Null
* `status`: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Architectural Design Decisions & Justifications
* **Patient Deletion Strategy:** If a patient profile is deleted, their associated records in the `appointments` table must **not** be cascade deleted. Medical history and past appointment data must be retained indefinitely for administrative, legal, and operational continuity. A soft-delete mechanism (e.g., an `is_active` flag) should be handled in the application layer.
* **Appointment Overlap Restrictions:** A doctor must not be allowed to have overlapping appointments. To prevent scheduling conflicts, a composite unique index on `(doctor_id, appointment_time)` will be enforced at the database level, complemented by validation checks in the service layer.
* **Data Validation:** Email and phone formats will be validated via backend application code (using Java regular expressions and annotations) before persisting to ensure clean, localized error reporting.

---

## Section 5: MongoDB Collection Design

### Collection: prescriptions
This document collection complements the structured MySQL schema by storing flexible, unstructured clinical data such as free-form medical notes, variable medication dosages, and metadata tracking.

```json
{
  "_id": "ObjectId('64abc1234567')",
  "appointmentId": 51,
  "patientId": 108,
  "patientName": "John Smith",
  "doctorName": "Dr. Sarah Jenkins",
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Take 1 tablet every 6 hours as needed"
    },
    {
      "name": "Amoxicillin",
      "dosage": "250mg",
      "frequency": "Take 1 capsule 3 times daily for 7 days"
    }
  ],
  "doctorNotes": "Patient presents with mild fever and symptoms of a localized sinus infection. Advised rest and high fluid intake.",
  "metadata": {
    "issuedAt": "2026-06-26T14:30:00Z",
    "clinicBranch": "Downtown Medical Center",
    "requiresFollowUp": true
  },
  "tags": [
    "Antibiotics",
    "Fever-Management",
    "Urgent"
  ]
}
