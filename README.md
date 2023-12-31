# CARD COST API

A Spring Boot REST API that provides: 
1. Create Read Update Delete operations on a clearing cost matrix table of the following format

| Card issuing country | Clearing Cost (USD) | 
|--------------------|---------------------|
| us                 | 5                   | 
| gr                 | 10                  | 
| other              | 15                  |

2. Clearing cost calculation for a given card number, utilizing the information provided by this public API https://bintable.com/get-api  

In case the BINTable Api responds with a country code, that is not registered in the clearing cost matrix, 
the fallback country code 'other' will be used for the cost calculation.  
The value for the fallback country code as well as the fallback cost, can be configured in application.properties file.


## Endpoints
The project is built on top of the Spring Boot framework and uses Redis to store the clearing cost matrix table data.
In this project, the following endpoints are implemented:

| HTTP Method | Path                      | Summary                                                |
|-------------|---------------------------|--------------------------------------------------------|
| POST        | /api/costs/create         | Create a clearing cost entry                           |
| GET         | /api/costs                | List all clearing cost entries                         |
| GET         | /api/costs/{country_code} | List a clearing cost entry given the iso2 country code |
| PUT         | /api/costs/update         | Update a clearing cost entry                           |
| DELETE      | /api/costs/delete         | Delete a clearing cost entry                           |
| POST        | /api/payment-cards-cost   | Retrieve the cost for a given card number              |


Detailed API documentation can be found here: 
https://ramsimmin.github.io/cardcost/

## Redis
Run a redis container 

`docker run --name=redis-server -p 6379:6379 -d redis:7.0.8`

## Setup application.properties
Update the api_key property under: `src/main/resources/application.properties` 

api.bintable.api_key=<your_api_key>

You can get one by signing up at: https://bintable.com/get-api or use the following one (balance sufficiency is not guaranteed)
`df39ee4320aa8221067864eda1c0f2844f901336`

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
Detailed information about the rest endpoints are documented with swagger. You can access the documentation deployed in github pages
https://ramsimmin.github.io/cardcost/

or by accessing the following url while the application is running:
http://localhost:8086/openapi/swagger-ui/index.html  