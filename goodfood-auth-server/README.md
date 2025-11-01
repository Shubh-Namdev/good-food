# GoodFood Auth Server

Spring Authorization Server-based Auth server for GoodFood.

## Run locally

1. Start a MySQL instance and create a database `goodfood_authdb`.
2. Update `src/main/resources/application.yml` if your MySQL credentials/host differ.
3. Build and run:

```bash
./gradlew bootRun
````

Endpoints:

* JWKs: [http://localhost:9000/oauth2/jwks](http://localhost:9000/oauth2/jwks)
* Token: [http://localhost:9000/oauth2/token](http://localhost:9000/oauth2/token)
* Authorization endpoint: [http://localhost:9000/oauth2/authorize](http://localhost:9000/oauth2/authorize)

To test client credentials (example client is pre-registered):

```bash
curl -u gateway-client:gateway-secret -d 'grant_type=client_credentials' http://localhost:9000/oauth2/token
```

````

---