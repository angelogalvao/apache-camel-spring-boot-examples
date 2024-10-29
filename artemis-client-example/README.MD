# Camel on Spring Boot: Artemis Example

## Description

This is an example application that uses Camel on Spring Boot runtime to connect to a ActiveMQ Artemis broker running on Openshift. 

## Deploy the application

### Deploy ActiveMQ Artemis in Openshift

1. Create the project
```bash
oc new-project artemis-camel-spring-boot-project
```

2. Install Red Hat AMQ (ActiveMQ Artemis) operator
```bash
oc create -f src/main/openshift/001-sub.yaml
```

3. Create the broker
```bash
oc create -f src/main/openshift/002-artemis.yaml
```

4. Create the service
```bash
oc create -f src/main/openshift/003-service.yaml
```


### Deploy the Camel Spring Boot Application

1. The dekorate plugin use images that are only available at Docker registry docker.io. Sometimes, Openshift is not able to download the images because docker.io is throttling requests from anonymous users. So, if you are facing that, add the registry credentials to give you an extra breath on the registy:

```bash
oc create secret docker-registry docker-registry \
    --docker-server docker.io \
    --docker-username USER \
    --docker-password PASSWORD \
    --docker-email=EMAIL

oc secrets link --for=pull default docker-registry
```

2. Deploy the application on Openshift:

```bash
mvn clean install -Ddekorate.deploy=true
```