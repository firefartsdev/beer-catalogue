# 🍺 Beer Catalogue API

API to manage a catalog of beers and their manufacturers.

---

## 🛠️ Setup and execution

The application is prepared to run in three different ways:

- Locally (for development)
- By starting a Docker container
- By deploying it to a cluster

### Locally

There is a properties filel `application-local.yml` with the necessary 
configuration to run the application locally and connect to a PostgreSQL 
database. For the database, a `docker-compose` file is provided y the 
`/docker-compose` folder to start a PostgreSQL container.

The application includes a service that uses an S3 bucket to manage beer
images. To enable this functionality, the application must be started
with the following environment variables:

- AWS_SECRET_KEY_ID
- AWS_SECRET_ACCESS_KEY

### Running the application out-of-the-box with Docker

To run the application quickly using Docker, follow these steps:

**1. Prepare the environment variables**  
In the `/docker-compose` folder, you will find an `.env.sample` file
that serves as a template showing which are the environment variables
you need to define. These variables are related with the S3 bucket and 
PostgreSQL database.

**2. Build and start the containers**  
From the project root, run the following command to build the Docker image
and start both the application and the PostgreSQL database using Docker
Compose.

```code
docker-compose up --build
```
This will:
- Build the applcation image using the provided Dockerfile
- Start the PostgreSQL database container with the settings from the 
`env` file
- Start the application container with the necessary configuration to 
connect to both the database and the S3 bucket.

**3. Access the application**  
Once the containers are up and running, the application will be accessible
at:

```code
http://localhost:8080
```

**4. API Testing**  
You can now test the API endpoints using a tool like Postman or cURL.
All available endpoints are documented in the `openapi.yaml` file
located at the resources folder.

### Deploying the application to a Kubernetes cluster with Helm

The application can be deployed to a Kubernetes cluster using Helm.

**1. Create the namespace**  
Apply the namespace manifest:
```code
kubectl appliy -f namespace.yaml
```

This will create the `beer-app` namespace, where all related resources
will be deployed.

**2. Create the required secrets**  
Make sure the following Kubernetes secrets are created in the `berr-app`
namespace:
- `s3-credentials`: Contains the necessary S3 environment variables:
    - `AWS_SECRET_KEY_ID`
    - `AWS_SECRET_ACCESS_KEY`
    - `AWS_REGION`
    - `AWS_S3_BUCKET_NAME`
- `aws-rds-db-credentials`
    - `DB_HOST`
    - `DB_PORT`
    - `DB_NAME`
    - `DB_USERNAME`
    - `DB_PASSWORD`

You can create the using `kubectl` like this:

```code
kubectl create secret generic s3-credentials \
  --from-literal=AWS_ACCESS_KEY_ID=your-access-key-id \
  --from-literal=AWS_SECRET_ACCESS_KEY=your-secret-key \
  -n beer-app

kubectl create secret generic aws-rds-db-credentials \
  --from-literal=DB_URL=jdbc:postgresql://your-host:5432/your-db \
  --from-literal=DB_USERNAME=your-username \
  --from-literal=DB_PASSWORD=your-password \
  --from-literal=DB_HOST=your-host \
  --from-literal=DB_PORT=5432 \
  --from-literal=DB_NAME=your-db \
  -n beer-app
```

**3. Review the Helm chart configuration**  
The main Helm configuration is defined in `values.yaml`:
```code
namespace: beer-app

app:
  image: beer-app:latest
  imagePullPolicy: Never
  port: 8080

secrets:
  s3: s3-credentials
  db: aws-rds-db-credentials
```
⚠️ The imagePullPolicy: Never setting ensures that the Kubernetes cluster
uses the image that has already been manually loaded into the cluster's 
local Docker registry or node cache, instead of trying to pull it from a 
remote registry.

If you are running a local Kubernetes cluster like Minikube, follow these
steps:

```code
eval $(minikube docker-env)
docker build -t beer-app:latest .
```

This command builds the Docker image directly inside the Minikube Docker
daemon, making it available to the cluster.

```code
docker build -t beer-app:latest .
kind load docker-image beer-app:latest --name your-kind-cluster
```

This command loads the image into the Kind cluster's internal image
store.

Once the image is available in the cluster and `imagePullPolicy: Never`
is set, Kubernetes will use it directly during deployment.

**4. Deploy the application**  
Install or upgrade the Helm release:

```code
helm upgrade --install beer-app ./path-to-your-chart --namespace beer-app
```

**5. Verify the deployment**  
Check that the pods and deployment are running correctly:

```code
kubectl get pods -n beer-app
kubectl get deployments -n beer-app
```

**6. Access the application**  
The application is exposed through a Kubernetes `Service` defined in the
`beer-app-service.yaml` file:
```code
apiVersion: v1
kind: Service
metadata:
  name: beer-app
  namespace: beer-app
spec:
  type: NodePort
  selector:
    app: beer-app
  ports:
    - port: 80
      targetPort: 8080
      nodePort: 30080
```

This service:
- Forwards external traffic from port `30080` on the node to port 
`8080` on the container
- Uses `type: NodePort`, making it accessible from outside the cluster

Apply the service manifest:
```code
kubectl apply -f beer-app-service.yaml
```

You can retrieve the URL to access the application using:
```code
minikube service beer-app -n beer-app --url
```

This will return output similar to:

```code
🏃  Starting tunnel for service beer-app.
|-----------|------------|-------------|-----------------------------|
| NAMESPACE |   NAME     | TARGET PORT |             URL             |
|-----------|------------|-------------|-----------------------------|
| beer-app  | beer-app   |        8080 | http://127.0.0.1:51843      |
|           |            |             | http://192.168.49.2:51843   |
|-----------|------------|-------------|-----------------------------|
```

Use one of the listed URLs to test the API in Postman or using cURL,
for example:

```code
curl http://192.168.49.2:51843/api/v1/beers
```

## Design decisions

The code is structured following the principles of **Hexagonal Architecture,
Domaing-Driven Desing (DDD)** and **Clean Code**. This promotes a clear 
separation of concerns, and maintainability over time.

The project is organized into three main layers:

- **Domain**:
This layer contains the core business model and domain logic. It defines the
entities, value objects, and interfaces (ports) that describe the problem
space independently of any technical concerns. It is completely agnostic 
frameworks or infrastructure.

- **Application**:
This layer implements the use cases and orchestrates the business rules defined 
in the domain. It coordinates interactions between the domain and the external
world through ports (interfaces), without being coupled to infrastructure
details. Business logic lives here.

- **Infrastructure**:
This layer is responsible for handling external concerns such as HTTP 
communication, persistence, and third-party services. In this project,
it includes:
    - A **REST adapter** for handling HTTP requests and responses
    - A **repository adapter** for interacting with the PostgreSQL database
    - An **S3 adapter** for managing image storage in an S3 bucket

This architectural approach ensures that the domain remains the most stable
and central part of the system, while the infrastructure can evolve or change
with minimal impact on the core business logic.

### Testing

The entire core of the business logic is covered by **unit tests**, ensuring
correctness, fast feedback, and maintainability of the application`s 
behavior.

In addition to unit tests, the project includes a suit of **integration
tests** that verify the interaction between components (e.g., HTTP layer, 
database...). These tests are fully self-contained and **do not require
any external services or Docker environment to be running** since the
project uses **Testcontainers**.

This approach provides a good balance between fast, isolated unit tests and
realistic, production-like integration tests.

### Bonus steps

Next, I will explain how the "Bonus steps" were approached and the key design
decisions made during their implementation.

#### Security

Ideally, role-based access should be handled by a dedicated authentication 
service that, based on a security token received in each REST request, provides
the necessary user role information to determine how to proceed. However, this
kind of setup is beyond the scope of this challenge, and I assumed that the
goal is to demonstrate the ability to configure and work with Spring
Security.

With that in mind, I opted for a simple and lightweight approach that is easy
to implement and use. The solution is based on sending the user's role via a
custom HTTP header name `X-User-Role` in each REST request.

A custom filter reads this header and injects the role into the Spring Security
context. On the other hand, a Spring Security configuration is defined to
restrict access to specific endpoints based on the user`s role. This setup
allows the application to simulate role-based authorization effectively.

#### Search and pagination

For listing beers and manufacturers, the application provides `GET` endpoints
that support **pagination and sorting** via `request parameters`. Clients can
specify parameters such as `page`, `size`, `order` and `sortBy` directly in
the query string of the request.

On the other hand, for more advanced search scenarios, a dedicated `POST`
endpoint is available. It accepts a **DTO containing search criteria**, along
with pagination and sorting options. This approach allows for greater 
flexibility in building complex queries.

To implement multi-field search against the database, I used `Spring Data
JPA Specifications`, which provide a dynamic and type-safe way to construct
queries based on the provided criteria. This makes the search logic clean,
composable and easy to maintain.

#### Picture upload

When implementing image upload as part of the beer creation process, my initial
thought was to take a quick route and store the image in Base64 format directly
in the database. However, I quickly dismissed this approach, as it's not a 
scalable or realistic solution and would not be acceptable in a real-world
scenario.

I then considered storing the image in a local `static` folder and saving the
file path in the database, as a way to simulate integration with an external 
service like S3. But while thinking through that approach, I realized that -
since I was already working on this - setting up a real S3 bucket and 
interacting with it would require about the same effort, and would make the
solution much more realistic and relevant.

So, in the end, I implemented the following behavior:

- When a beer is created, the image is uploaded to an **S3 bucket**.
- If the creation of the beer in the database fails for any reason after the
image upload, the image is automatically deleted from the bucket to avoid 
orphaned files.
- Since the image is already in S3, I also enhanced the `GET /beers/{beerId}`
endpoint to include the downloaded image from the bucket in the response.

This approach simulates a production-ready image handling workflow while keeping
the implementation clean and consistent.

#### Cloud deployment

For the cloud deployment part of the challenge, I opted to use **Minikube**
as a local Kubernetes cluster to simulate a realistic deployment environment.

Initially, I created the necessary **Kubernetes YAML manifests** to deploy the
application using standard `kubectl` commands. These configuration files are 
available in the `k8s/` directory, and include definitions for the namespace,
deployment, service and other relevant resources.

Once I had the application successfully running on Minikube, I noticed that in 
the next step of the challenge -which involves integrating with an AWS 
RDS-hosted database- the instructions specifically mentioned using **Helm**
for the deployment configuration. To align with that requirement, I adapted
the deployment setup into a **Helm chart**, which is now located in the 
`helm/` directory.

#### AWS-Hosted Database

While migrating the deployment configuration to Helm, I also added the necessary
settings to connect the application to a **PostgreSQL database hosted on
AWS RDS**.

The database connection details are injected into the application via 
**Kubernetes Secrets**, whose names are defined in the `values.yaml` file
of the Helm chart. These secrets are referenced in the deployment template and
mapped to environment variables expected by the application.

While configuring the connection to the AWS RDS database, I encountered a couple
of issues:

- Initially, I attempted to construct the database URL dynamically by
interpolating individual environment variables such as `DB_HOST` and
`DB_PORT`. However, it turns out that **Spring Boot does not support
environment variable interpolation inside property values** (e.g. using 
something like `jdbc:postgresql://${DB_HOST}:${DB_PORT}/...`). As a result,
the URL was not resolved correctly, and the application failed to start.
This issue caused some headaches unit I found out the problem. I eventually
fixed it by passing the full `DB_URL` as a single environment variable set
explicitly in the Kubernetes secrets.
- Another issue I encountered was related to **access permissions**. When I 
first created the RDS instance, I forgot to configure an appropiate **security
group policy** to allow inbound connections from my Kubernetes cluster. As a
result, the application could not reach the database even though the 
configuration appeared correct. Once I updated the RDS security group to allow
connections from the cluster's IP range, the connection worked as expected.


As a final note regarding the cloud deployment, I should mention that I was 
already familiar with interacting with a Kubernetes cluster -scaling pods,
inspecting logs, and reviewing deployment configurations- but I had 
**never actually deployed a pod from scratch myself**, as this has typically
been handled by infrastructure or DevOps teams. That said, I found this
experience both **fun and valuable**, as it gave me the opportunity to learn
and practice working more hands-on with `kubectl`.

Regarding the database, although I had worked on projects that used AWS RDS, I
had **never created and configured an RDS instance from scratch**.- I'm glad I
completed this part of the challenge as well, since it allowed me to discover
and learn new things about provisioning and securing managed databases in AWS.

## API usage examples

Next, I will provide some example `curl` commands to test API endpoints.

### GET - List manufacturers

```code
curl --location 'localhost:8080/api/v1/manufacturers?order=ASC&sortBy=country'
```

### POST - Create beer

```code
curl --location 'localhost:8080/api/v1/beers' \
--header 'X-User-Role: ADMIN' \
--form 'name="testing environment variables"' \
--form 'abv="5.2"' \
--form 'type="IPA"' \
--form 'description="with image"' \
--form 'manufacturerId="162e9188-bf65-4e4e-a91f-7545960f595f"' \
--form 'image=@"/C:/Users/leafa/Downloads/beer2.jpg"'
```

### PUT - Update manufacturer

```code
curl --location --request PUT 'localhost:8080/api/v1/manufacturers/d4ebd7a8-c5fc-4e79-b2f0-c75571b984b0' \
--header 'Content-Type: application/json' \
--header 'X-User-Role: ADMIN' \
--data '{
    "name": "Manufacturer test 2 updated",
    "country": "Spain updated"
}'
```

### DELETE - Delete beer
```code
curl --location --request DELETE 'localhost:8080/api/v1/manufacturers/1dbcae9a-99a3-4f94-8e81-563aa08bb03e' \
--header 'X-User-Role: ADMIN'
```

### POST - Search beer
```code
curl --location 'localhost:8080/api/v1/beers/search' \
--header 'X-User-Role: ADMIN' \
--header 'Content-Type: application/json' \
--data '{
    "name": null,
    "abv": null,
    "manufacturerName": "Test"
}'
```




