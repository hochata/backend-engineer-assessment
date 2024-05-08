# Code assessment

## General design

I tried to follow the existing conventions and structure of the project. In particular I started out by completing the method stubs and implementing the required interfaces. When the functionality I was trying to implement was better in a separate class, I tried to add all the new pieces of code as new, small, testable services. The final version is meant to be run from the Docker image, but the local version can still be used.

## Setup

### Prerequisites

To run the application you would require:

- [Java](https://adoptium.net/temurin/releases/)
- [Docker](https://docs.docker.com/get-docker/) with [Docker Compose](https://docs.docker.com/compose/)
- [Stripe API Keys](https://stripe.com/docs/keys)

### Stripe API Keys

Sign up for a Stripe account and get your API keys from the [Stripe Dashboard](https://dashboard.stripe.com/apikeys). Then in `application.properties` file add the following line with your secret key.

```properties
stripe.api-key=sk_test_51J3j
```

If you do not add this key then the application will not work.

## Run the application

### Run directly

Run the application using the following command or using your IDE.

```sh
./gradlew bootRun
```

### Run in Docker

Ensure that the Docker daemon is running, then run

```sh
docker compose up
```

It will take a few minutes to startup.

### Run tests

Run the unit tests with
```sh
./gradlew test
```

## Test the API

Get the accounts

```sh
curl 'http://localhost:8080/accounts'
```

Create a new account

```sh
curl --header 'Content-Type: application/json' --data \
'{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@doe.test"
  }' \
'http://localhost:8080/accounts'
```

Update existing account

```sh
 curl --request PATCH --header 'Content-Type: application/json' --data \
'{
  "firstName": "Alice"
 }' \
 'http://localhost:8080/accounts/<accountId>'
```
