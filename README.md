# fiap-tech-challenge-parquimetro

Coleção para ser importada no POSTMAN: 
```
src/main/resources/FIAP - Parquimetro.postman_collection_v2.json
```

OpenAPI Swagger:
```
http://localhost:8080/parquimetro/swagger-ui/index.html#/
```
Importante: o sistema considera que cada hora estacionado são R$ 10,00

Install and configure Localstack:
```shell
python3 -m pip install localstack
export PATH=$PATH:~/.local/bin
localstack --version
pip install awscli-local

export AWS_ACCESS_KEY_ID="test"
export AWS_SECRET_ACCESS_KEY="test"
export AWS_DEFAULT_REGION="us-east-1"
aws configure --profile default
```
To run locally, first:
```shell
docker-compose up
```
It will start MongoDB and Localstack with the SNS mocked topic
We can check the health of the Localstack services by accessing http://localhost:4566/health, the response should be a JSON in the following format:
```
{
 "services": {
   "dynamodb": "running",
   "s3": "running",
   "sns": "running",
   "sqs": "running",
   "kinesis": "disabled"
 }
}
```
Mongo Express em http://0.0.0.0:8081/

To configure localstack aws credentials edit your ~/.aws/config and include:
```shell
[default]
aws_access_key_id = test
aws_secret_access_key = test
region = us-east-1
```

#### Cria a fila com o nome "recibos-tempo-estacionamento-sqs"
```shell
awslocal sqs create-queue --queue-name "recibos-tempo-estacionamento-sqs"
```