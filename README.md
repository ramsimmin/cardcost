# CARD COST API

A Spring Boot REST API that provides: 
* Create Read Update Delete operations on a clearing cost matrix table
* Retrieve of clearing cost for a given card number, utilizing the information provided by this public API https://bintable.com/get-api

The project is built on top of the Spring Boot framework and uses Redis to store the clearing cost matrix table data.
In this project, the following endpoints are implemented:

| HTTP Method | Path                      | Summary                                                |
|-------------|---------------------------|--------------------------------------------------------|
| POST        | /api/costs/create         | Create a clearing cost entry                           |
| GET         | /api/costs                | List all clearing cost entries                         |
| GET         | /api/costs/{country_code} | List a clearing cost entry given the iso2 country code |
| PUT         | /api/costs/update         | Update a clearing cost entry                           |
| DELETE      | /api/costs/delete         | Delete a clearing cost entry                           |


## Redis
Run a redis container 

`docker run --name=redis-server -p 6379:6379 -d redis:7.0.8`

## Setup application.properties
Update the api_key property under: `src/main/resources/application.properties` 

api.bintable.api_key=<your_api_key>

You can get one by signing up at: https://bintable.com/get-api 

## Build the application

`mvn clean package -DskipTests`

## Run the application

You can run the application using your IDE or by executing:

`mvn spring-boot:run`

## Using docker

Build the app image   
`docker build -t card-cost-app . `

Run the container  
`docker run -p 8086:8086 -d -e SPRING_DATA_REDIS_HOST='host.docker.internal' card-cost-app`  

or by overwriting the api_key as well

`docker run -p 8086:8086 -d -e SPRING_DATA_REDIS_HOST='host.docker.internal' -e API_BINTABLE_API_KEY='df39ee4320aa8221067864eda1c0f2844f901336' card-cost-app`


## Open Api
Detailed information about the rest endpoints are documented with swagger. You can access the documentation while the application is running:
http://localhost:8086/openapi/swagger-ui/index.html  

