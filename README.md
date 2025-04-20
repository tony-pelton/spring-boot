<div align="center">

<img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot" alt="Spring Boot" />
<img src="https://img.shields.io/badge/Eureka-Discovery-blue?logo=spring" alt="Eureka" />
<img src="https://img.shields.io/badge/H2-Database-lightgrey?logo=databricks" alt="H2" />
<img src="https://img.shields.io/badge/Admin-UI-orange?logo=monitor" alt="Admin UI" />

# 📚 Spring Boot Bookstore

</div>

---

> **A modern, multi-module Spring Boot e-commerce sample stack.**
> 
> 🚀 *Best practices, rapid bootstrapping, and fun tech to play with!*

---

## ✨ Purpose

A sample Spring Boot application stack for an e-commerce bookstore, designed to be clean, best-practice, and ready to run out of the box with minimal setup. 

> *Not strictly microservices, but a playground for Spring, Eureka, Admin, and more!*

---

## 🖥️ Servers & Components

| Server/Module     | Capability              |
|-------------------|-------------------------|
| 🛒 `web`          | UI/Storefront           |
| 🗄️ `services`    | H2 Database, Boot Admin |
| 🛠️ `integration` | Spring Integration      |
| 📦 `warehouse`    | Warehouse Service       |
| 💻 `command`      | Command Line Bootstrap  |

- The database is **in-memory** (H2). Schemas are auto-created.
- Data is loaded from the `command` application at startup.

---

## 🌟 Features

- 🟢 **Spring Boot 3**
- 🛰️ **Eureka Discovery**
- 🖥️ **Spring Boot Admin**
- 🗄️ **H2 DB Server** (with H2 Console)
- 🔗 **Spring Integration**
- 💻 **Spring Command Line**
- 📦 **Maven Multi-Module**
- ✨ **Gemini API Integration** (book summaries if API key provided)
- 🛡️ **Spring Security**
- 🤖 **HAL Browser** (Hypermedia API explorer)

---

## 🚦 How To Run

1. **Start the services in order:**

   ```sh
   # Start the discovery server & H2 DB first
   ./mvnw spring-boot:run -pl services

   # Then the integration server
   ./mvnw spring-boot:run -pl integration

   # Then the warehouse server
   ./mvnw spring-boot:run -pl warehouse

   # Then the web server (storefront)
   ./mvnw spring-boot:run -pl web

   # (Optional) Start the command server to bootstrap data
   ./mvnw spring-boot:run -pl command
   ```

   You can also run each server's `main()` directly from your IDE.

2. **Access the UIs:**
   - 🛒 Web Storefront: [http://localhost:8080](http://localhost:8080)
   - 🌐 Eureka Discovery UI: [http://localhost:8081](http://localhost:8081)
   - 🗄️ H2 Console: [http://localhost:8082/h2-console](http://localhost:8082/h2-console)
   - 🖥️ Spring Boot Admin UI: [http://localhost:8081/admin](http://localhost:8081/admin)
   - 🤖 HAL Browser (Web): [http://localhost:8080/api](http://localhost:8080/api)
   - 🤖 HAL Browser (Warehouse): [http://localhost:8083/api](http://localhost:8083/api)

3. **Command Server:**
   - Only needs to be running when issuing commands (e.g., to bootstrap data).
   - Type `help` in the CLI to see available commands.

---

## 📝 Notes

- This project is intentionally a bit over-engineered for learning and experimentation.
- It demonstrates how much you can do with Spring Boot's ecosystem without full-blown cloud infrastructure.
- **Gemini API:** If you provide a Gemini API key, the integration service will enrich book data with AI-generated summaries.
    - Set your API key in [`integration/src/main/resources/application.yml`](integration/src/main/resources/application.yml) under the `apikey` property.

---

<div align="center">
    <sub>Made with ❤️ using Spring Boot, Eureka, and friends.</sub>
</div>


Once discovery is warmed up, run the 'load-books' and 'load-users' commands to
to get some fake data loaded.

You have to have run 'load-users' in order to login to the webapp.

### Notes

* if you want the Gemini integration to work, add your own apikey to the
application.yml for the integration service.
