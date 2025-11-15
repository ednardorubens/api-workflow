# API WORKFLOW
Aplicação de exemplo de uso do Spring AOP pra transição de status de um processo.
Projeto desenvolvido utilizando:
- **[Spring Boot](https://spring.io/projects/spring-boot)**
- **[Spring AOP](https://docs.spring.io/spring-framework/reference/core/aop.html)**
- **[Spring Actuator](https://docs.spring.io/spring-boot/reference/actuator/index.html)**
- **[Lombok](https://projectlombok.org/)**
- **[JaCoCo](https://www.eclemma.org/jacoco/)**
- **[JUnit5](https://junit.org/junit5/docs/current/user-guide/)**

# Requisitos para executar o projeto
- [Git](https://git-scm.com/)
- [JDK 17+](https://www.oracle.com/br/java/technologies/javase/jdk17-archive-downloads.html)

# Como executar o projeto
- Clone o projeto.
```bash
  git clone https://github.com/ednardorubens/api-workflow.git
```
- Entre na pasta do projeto.
```bash
  cd api-workflow
```
- Execute o comando abaixo para compilar o projeto.
```bash
  ./gradlew clean build
```
- Execute o comando abaixo para iniciar o banco de dados Mysql no docker e subir o projeto.
```bash
  ./gradlew bootRunDev
```
- Acesse o swagger do projeto em http://localhost:8080/swagger-ui.html.
