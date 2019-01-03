# A sample REST API microservice for Production Orders management implemented with JavaEE 8

Currently implemented API for the following life time operations of the production order:

1.  Create a new production order
2.  Set the production order to running mode
3.  The production order can also be stopped and restarted
4.  Finish the order


# Building and running on Docker (for Linux, version for Windows to be done yet)

./buildAndRun.sh
 

'sudo' command used in buildAndRun.sh can be removed if you can add the current user to the 'docker' group with the following command:
 
sudo usermod -a -G docker $USER


# Manual testing with Postman 

postman.json
postman_collection.json


# Domain description

Production orders have the following properties: order number, status (NEW, RUNNING, STOPPED, FINISHED), start time, end time. 

Production orders are executed in a specific production line. 
The user defines which product is produced and when the order should be started.
 
Production line, Product and User APIs are not part of the scope. Data related to them is preconfigured.


# Considerations

- ProductionOrder 'start time' is not to be updated on resuming the order's processing
- transition from STOPPED state to FINISHED state is only allowed through RUNNING
- updating an order status with the current value will not produce any errors (update ignored, warning to logs).


# Ideas and TODOs

- add validation so that an order execution cannot be started before it's start time
- track order execution total time
- pagination for orders listing
- to keep ProductionOrder valid status transitions in a list(?) so Status.isValidTransition() method code gets simple
- generic toString() method for value objects?
- i18n

- Postgre in an own container
- pre-populated environment dependant configurations
- real persistence layer implementation (Entity EJBs?)

- security (authentication, authorisation, https,.. etc.)
- versioning for the API (https://restfulapi.net/versioning)

- more tests
- automated tests for the API (currently only have Postman manual testing samples)
- stress testing (performance and scalability)

- logging configuration to be completed (env:LOG_ROOT)
- WELD-000146 CDI deprecation warnings?
- other Dockerfile improvements
- externalize all the environment dependant config to a separate project (logging etc.)
- extract client classes - value objects, exceptions, service facade EJB interface (to be pulled yet) to a separate module 

- migrate from Glassfish to Wildfly
- HA (Kubernates or/and Wildfly clustering)

