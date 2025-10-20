# Blood Bank Management System

## 📌 Project Description

The **Blood Bank Management System** is a monolithic web application developed in **Java 17** using **Jakarta EE**.
It helps hospitals efficiently manage **donors** and **receivers**, automating the matching process based on blood group compatibility and medical urgency levels.

### Main Features:

- **Donor Management**: create, update, and automatically validate eligibility (age, weight, medical history).
- **Receiver Management**: create, update, track needs based on urgency (NORMAL, URGENT, CRITICAL).
- **Automatic Matching**: associate donors to compatible receivers based on blood groups.
- **User Interface**: JSP pages with JSTL for dynamic interaction.
- **MVC Architecture**: clear separation of presentation, service, and data access layers.
- **Data Persistence**: using JPA/Hibernate with a relational database.

---

## 🛠️ Technologies Used

- **Language**: Java 17
- **Application Server**: Apache Tomcat
- **Framework**: Jakarta EE
- **Database**: MySQL or PostgreSQL (choice)
- **User Interface**: JSP, JSTL, CSS (native or frameworks such as Bootstrap/Tailwind)
- **Build & Project Management**: Maven
- **Unit Testing**: JUnit
- **Error Handling**: display validation and other errors
- **ORM**: JPA/Hibernate
- **Architecture**: MVC, SOLID design patterns

---

## 📂 Project Structure

```
sang-link/
├── src/
│   ├── main/
│   │   ├── java/com/sanglink/
│   │   │   ├── config/                  # Configuration (e.g., JpaBootstrapListener)
│   │   │   ├── controller/              # Servlets and handler classes
│   │   │   │   └── handler/
│   │   │   ├── dao/                      # DAO interfaces and implementations
│   │   │   │   └── impl/
│   │   │   ├── dto/                      # Request DTOs
│   │   │   │   └── request/contract/
│   │   │   ├── entity/                   # JPA entities and enums
│   │   │   │   └── enums/
│   │   │   ├── exception/                # Custom exceptions
│   │   │   ├── mapper/                   # Mapper classes
│   │   │   ├── repository/               # Repository interfaces and implementations
│   │   │   │   └── impl/
│   │   │   ├── service/                  # Service interfaces and implementations
│   │   │   │   └── Impl/
│   │   │   └── util/validation/request/  # Validators for request DTOs
│   │   ├── resources/META-INF/           # persistence.xml
│   │   └── webapp/
│   │       ├── view/                     # JSP views
│   │       │   ├── donors/
│   │       │   ├── receivers/
│   │       │   └── fragments/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       └── index.jsp
├── test/                                 # Unit tests
├── .env
├── .env.example
├── .gitignore
└── pom.xml
```


---

## 📄 Class Diagram

<img width="941" height="979" alt="sanglink drawio" src="https://github.com/user-attachments/assets/9aa4d4bd-8ac0-477b-b88f-fb36510de318" />

---

## ✅ Main Features

- Create and manage donors: add, edit, delete, with automatic eligibility validation.
- Create and manage receivers: add, edit, delete, track needs based on urgency.
- Match donors to receivers automatically according to blood group compatibility.
- Dynamic JSP interface using JSTL.
- Error handling with clear messages for validation or system errors.
- MVC architecture separating presentation, service, and DAO layers.
- Data persistence using JPA/Hibernate with relational database.

---

## 📸 Screenshots

<img width="1909" height="843" alt="image" src="https://github.com/user-attachments/assets/209de948-df69-4cf7-93ef-aefee348714f" />

---

<img width="1919" height="779" alt="image" src="https://github.com/user-attachments/assets/71f1b895-d3ff-4df7-92b5-fa4e93865782" />

---

## 🛠️ Installation & Execution

### 1️⃣ Clone the repository

```bash
git clone https://github.com/ahmedbenkrarayc/sang-link.git
cd sang-link
```

### 2️⃣ Build the project with Maven

```bash
mvn clean package
```

### 3️⃣ Deploy the application using Tomcat7 Maven plugin

```bash
mvn tomcat7:deploy
```

- This command deploys the generated `.war` file to your configured Tomcat server.  
- Access the application at `http://localhost:8080/sang-link`.  

### 4️⃣ Run unit tests

```bash
mvn test
```

> Unit tests validate donor eligibility, receiver status, and matching logic.

