# Note Project

### Running
Requirements:
 - Docker
 - Docker-compose

The compose configuration uses the profiles `frontend-dev` and `backend-dev`.
Example of running:
```
docker-compose --profile frontend-dev up --build
```
Note: When running the `backend-dev`-profile the `JDBC_URL`-env variable needs to be set to accommodate the service not being orchestrated by docker-compose:
```
export JDBC_URL="jdbc:postgresql://0.0.0.0:5432/postgres"
```