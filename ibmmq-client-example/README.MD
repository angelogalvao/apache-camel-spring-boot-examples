# Camel on Spring Boot: IBM MQ Example

## Description

This is an example application that uses Camel on Spring Boot runtime to connect to a IBM MQ broker running on Openshift. 

## Deploy the application

### Deploy IBM MQ Artemis in Openshift

1. Create the project
```bash
oc new-project ibmmq-camel-spring-boot-project
```

2. Install IBM Catalog. 
```bash
oc create -f src/main/openshifft/001-catalog.yaml
```

3. Install IBM MQ operator
```bash
oc create -f src/main/openshift/002-sub.yaml
```

4. Create the IBM MQ password secret
```bash
oc create secret generic my-mq-dev-passwords --from-literal=dev-admin-password=passw0rd --from-literal=dev-app-password=passw0rd
```

5. Create the broker
```bash
oc create -f src/main/openshift/003-ibmmq.yaml
```

### Access IBM MQ web console

1. Get the web console URL:
```bash
oc get routes qm-dev-ibm-mq-web
```

2. Access the Web console in the browser.
   - User: admin
   - Password: passw0rd


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
./mvnw clean install -DSkipTests -Ddekorate.deploy=true
```