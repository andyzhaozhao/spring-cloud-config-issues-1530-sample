### Summery
The sample project for Spring Cloud Config's issue: https://github.com/spring-cloud/spring-cloud-config/issues/1530

### Branches Description
* **master**: based on **Spring Cloud Hoxton.SR3**. The problem is "Could not fetch the dynamic Eureka clients when using Spring Cloud Config"
* **masterG**: based on **Spring cloud Greenwich.SR5**. Every thing runs well.

### Modules Description
* **eureka-server**: The Eureka Server
* **config-serve**r: The Sping Cloud Config Server
* **gateway**: The Spring Cloud Gateway
* **micro-service**: A simple Spring Cloud Micro Service

### How to run & Reproduce the bug 
#### In branch `master`(Spring Cloud Hoxton.SR3)
##### 1. Reproduce the bug 
1. run eureka server: `mvn -f eureka-server spring-boot:run`
1. run config server: `mvn -f config-server spring-boot:run`
1. run gateway : `mvn -f gateway -P native spring-boot:run`
    1. show gateway routes: visit url "http://localhost:8090/actuator/gateway/routes" by chrome, return this:
    ```
        [{"predicate":"Paths: [/config-server/**], match trailing slash: true","metadata":{"management.port":"8710"},"route_id":"ReactiveCompositeDiscoveryClient_CONFIG-SERVER","filters":["[[RewritePath /config-server/(?<remaining>.*) = '/${remaining}'], order = 1]"],"uri":"lb://CONFIG-SERVER","order":0}]          
    ```
   there is no "micro-service" because I haven not started the "micro-service".
1. run micro-service: `mvn -f micro-service -P native spring-boot:run`
    1. visit url "http://localhost:8761", there is 3 service: config-server，gateway and micro-service
    1. show gateway routes: visit url "http://localhost:8090/actuator/gateway/routes" by chrome, return this:
    there is no micro-service's route. nothing happened！The gateway should be collect the routes dynamically.
The Bug:  Could not fetch the dynamic Eureka clients when using Spring Cloud Config

##### 2. Provement: everything runs well without “config-server”
1. run eureka server: `mvn -f eureka-server spring-boot:run`
1. run gateway,remove `-P native`: 
        `mvn -f gateway spring-boot:run `
    1. show gateway routes: visit url "http://localhost:8090/actuator/gateway/routes" by chrome, return this:
    ```
        [{"predicate":"Paths: [/config-server/**], match trailing slash: true","metadata":{"management.port":"8710"},"route_id":"ReactiveCompositeDiscoveryClient_CONFIG-SERVER","filters":["[[RewritePath /config-server/(?<remaining>.*) = '/${remaining}'], order = 1]"],"uri":"lb://CONFIG-SERVER","order":0}]          
    ```
   there is no "micro-service" because I have not started the "micro-service".
1. run micro-service,remove `-P native`:   
        `mvn -f micro-service spring-boot:run `
    1. visit url "http://localhost:8761", there is 2 service: gateway and micro-service
    1. show gateway routes: visit url "http://localhost:8090/actuator/gateway/routes" by chrome, return this:
    ```
        [{"predicate":"Paths: [/gateway/**], match trailing slash: true","metadata":{"management.port":"8090"},"route_id":"ReactiveCompositeDiscoveryClient_GATEWAY","filters":["[[RewritePath /gateway/(?<remaining>.*) = '/${remaining}'], order = 1]"],"uri":"lb://GATEWAY","order":0},{"predicate":"Paths: [/micro-service/**], match trailing slash: true","metadata":{"management.port":"8070"},"route_id":"ReactiveCompositeDiscoveryClient_MICRO-SERVICE","filters":["[[RewritePath /micro-service/(?<remaining>.*) = '/${remaining}'], order = 1]"],"uri":"lb://MICRO-SERVICE","order":0}]
   ```
    There are some micro-service's routes. It proves that The gateway collected the routes dynamically without "config-server"

#### In branch `masterG`(Spring cloud Greenwich.SR5) everything runs well
##### Provement: everything runs well with “config-server” in G
1. change branch to master G:
1. run eureka server: `mvn -f eureka-server spring-boot:run`
1. run config server: `mvn -f config-server spring-boot:run`
1. run gateway : `mvn -f gateway -P native spring-boot:run`
1. run micro-service: `mvn -f micro-service -P native spring-boot:run`  

Visit url "http://localhost:8090/actuator/gateway/routes" by chrome, return this:
There are some micro-service's routes. It proves that The gateway collected the routes dynamically with "config-server".

### gateway 
* http://localhost:8090/actuator/gateway
* http://localhost:8090/actuator/gateway/routes
* http://localhost:8090/actuator/gateway/routes/{id}
* http://localhost:8090/actuator/gateway/globalfilters
* http://localhost:8090/actuator/gateway/routefilters
* http://localhost:8090/actuator/gateway/refresh


### config server
* http://localhost:8710/micro-service/native
* http://localhost:8710/gateway/native