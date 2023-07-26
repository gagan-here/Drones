# Drone Management System

The Drone Management System is a REST API-based service for managing a fleet of drones and their loaded medications. It provides endpoints for registering drones, loading medications, checking loaded medications, checking available drones, and checking drone battery levels.

## Requirements

- Java 8 or higher
- Maven

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/drone-management-system.git
   
2. Build the project:
    ```bash
    mvn clean install

3. Run the application:
    ```bash
   mvn spring-boot:run

The application will start running on `http://localhost:8080`

## API Endpoints

| HTTP Method | Endpoint                            | Description                                |
|-------------|-------------------------------------|--------------------------------------------|
| POST        | `/drones`                           | Register a new drone.                       |
| POST        | `/drones/{serialNumber}/load`       | Load medications to a specific drone.       |
| GET         | `/drones/{serialNumber}/loaded`     | Get the loaded medications for a specific drone. |
| GET         | `/drones/available`                 | Get the available drones for loading.       |
| GET         | `/drones/{serialNumber}/battery`    | Check the battery level of a specific drone. |


## Usage 
Use a tool like Postman or curl to send HTTP requests to the API endpoints. Here are some examples:
* Register a drone: `POST /drones` <br>
  Request Body: <br>
    ```json
    {
       "serialNumber": "DRN001",
       "model": "HEAVYWEIGHT",
       "weightLimit": 500,
       "batteryCapacity": 80,
       "state": "IDLE"
    }

* Load medication to a drone:`POST /drones/DRN001/load` <br>
  Request Body: <br> 
    ```json
    [
      {
           "name": "Medication 1",
           "weight": 200,
           "code": "M001",
           "image": "medication1.jpg"
      }
    ]

* Get loaded medications for a drone: `GET /drones/DRN001/loaded`

* Get available drones for loading: `GET /drones/available`

* Check battery level for a drone: `GET /drones/DRN001/battery`

## Contributing
**Contributions are welcome!** If you have any suggestions, bug reports, or feature requests, please open an issue or submit a pull request.