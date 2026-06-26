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
