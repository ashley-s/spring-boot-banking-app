# Spring Boot Testing with a mini banking app in an Hexagonal Architecture
## Introduction
This was supposed to be my talk at the MSCC DevCon 2022 but due to some unforeseen circumstances, I could not present at the conference. I put up quite some work in that
and definitely want to share that with you all, especially Beginners and Intermediate Developers too.
To be honest, I have heard the hexagonal architecture only this year and I wanted to experience with that architecture instead of the classical way we do things
usually. The application I have built is a mini banking app, a very basic app. It is an easy one to follow and also the tests as well in this domain would be
make more sense.
My talk is focused mostly on how to write unit and integration testing for a Spring Boot App and to see the capabilities
offered by the Framework
## Architecture of the Application
![img.png](img.png)
The main idea, IMHO, behind a hexagonal architecture is to keep the domain logic free/independent of any
framework so that the code can be easily transported that to any framework. The mini application consists of the following features:
1. Get bank accounts for a customer.
2. Get transactions for a particular bank account number.
3. Perform a transfer between two bank accounts.
4. The application will generate an event upon a successful account transfer.
### Setting up of the application
You will need keycloak up and running to be able to run the application. Below are the docker-compose commands:
```yaml
version: '2'
services:
  postgresql:
    image: docker.io/bitnami/postgresql:11
    environment:
      # ALLOW_EMPTY_PASSWORD is recommended only for development.
      - ALLOW_EMPTY_PASSWORD=yes
      - POSTGRESQL_USERNAME=bn_keycloak
      - POSTGRESQL_DATABASE=bitnami_keycloak
    volumes:
      - 'postgresql_data:/bitnami/postgresql'

  keycloak:
    image: docker.io/bitnami/keycloak:19
    depends_on:
      - postgresql
    ports:
      - "80:8080"

volumes:
  postgresql_data:
    driver: local

```
Once up, please import the [realm](test-realm.json). All configuration will be done accordingly.
I am also attaching the postman collection for you to test. Download [here](Banking%20App.postman_collection.json). Once you are done importing the postman collection and
you are getting successful responses, I invite you to go through the code and try to understand. I did my optimum best to write comments throughout the code. I will focus more on the 
tests though in this documentation.
### Testing Strategy
I am sure that you know the testing pyramid and that integration and end-to-end tests are quite expensive to write. So you need
to focus mostly on unit testing as they give you fast feedback, blabla and blabla. LOL!
You can definitely pick up some books and read on this. Let's not waste time.
### Domain layer testing
For this part, you would agree with me that since