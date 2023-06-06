# SolarSimulator

SolarSimulator is an application that accurately simulates solar charging network performance and energy output, helping users optimize their solar investments and support capacity planning.

## Prerequisites

- Java 17
- Maven

## Build and Run the Application

1. Navigate to the project directory in your terminal.
2. Build the project with Maven:

The application will start and be accessible at `http://localhost:8080`.

`mvn clean install`

2. Run the application:

`mvn spring-boot:run`

The application will be available at http://localhost:8080.

You can also run the application with command-line arguments by enabling the `runWithArgs` property in the `application.yml` file:

```yaml
app:
  runWithArgs: true
```

When this property is set to true, you must pass command-line arguments to the application when running it like: `<path_to_json_file> <elapsed_days>`


## API Endpoints
The APIs can be accessed at http://localhost:8080/swagger-ui.html. The following REST endpoints are available:

- `POST /solar-simulator/load`: Load a solar charging network into the application. The request body should be a JSON array of grid objects, each with a name and age property.
- `GET /solar-simulator/output/T`: Get the total output of the solar charging network at T days.
- `GET /solar-simulator/network/T`: Get the state of the solar charging network at T days.
- `GET /solar-simulator/get-network`: Get the current network.

## User Interface
The application features a simple, intuitive web interface built with Thymeleaf, accessible at `http://localhost:8080`. This interface allows users to:

- View the current state of the solar network, including a graphical representation
- Add new solar grids to the network
- Remove existing solar grids from the network
- Upload a JSON file to load network data

## Logging

The application's logs are configured using the `logback.xml` file located in the `src/main/resources` folder. Logs will be recorded in the `/logs` directory under the project root.

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
- [Maven](https://maven.apache.org/) - Dependency Management
- [Lombok](https://projectlombok.org/) - Boilerplate code reduction
- [Springdoc OpenAPI](https://springdoc.org/) - API documentation generation
- [Springdoc OpenAPI UI](https://springdoc.org/) - API documentation user interface
- [Thymeleaf](https://www.thymeleaf.org/) - A modern server-side Java template engine

## Tests

The project includes both unit and integration tests available in the `test` package. The following technologies have been used for testing:

- JUnit
- Mockito
- TestRestTemplate

## Author

- **Payam Soudachi** - [psouda.com](http://psouda.com/)
    - Email: [psoudachi@gmail.com](mailto:psoudachi@gmail.com)