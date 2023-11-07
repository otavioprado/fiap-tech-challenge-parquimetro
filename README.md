# APP PARQUIMETRO
O novo sistema de parquímetro foi projetado para lidar com a demanda crescente de estacionamento na
cidade. Ele oferece funcionalidades tais, como registro de condutores e veículos, controle de tempo estacionado,
opções flexíveis de pagamento e emissão de recibos.

Importante: o sistema considera que cada hora estacionado são R$ 10,00
## Tecnologias e ferramentas utilizadas:

* [Spring initializr]( https://start.spring.io/): Ferramenta para criação de projetos Spring Boot
* [Spring Boot]( https://spring.io/projects/spring-boot): Framework Web
* [Maven]( https://mvnrepository.com/): Gerenciador de dependências do projeto
* [Lombock] Dependência para requisições HTTP
* [Postman](https://www.postman.com/): Ferramenta para teste de requisições HTTP
* [Open Api - Swagger] Dependência para gerar interface amigável para testes
* [mongoDb] Banco de dados noSQL
* [Localstack] Dependência para simular serviços da AWS
* [Mongo Express] Dependência para visualizar o banco de dados mongoDb
* [Docker] Dependência para criar containers
* [Docker Compose] Dependência para orquestrar containers
* [AWS CLI] Dependência para interagir com a AWS

## Instalação e configuração

Para rodar o projeto é necessário ter instalado:
* [Java 17](https://www.oracle.com/br/java/technologies/javase-jdk17-downloads.html)
* [Maven](https://maven.apache.org/download.cgi)
* [MongoDB](https://www.mongodb.com/try/download/community)
* [Docker](https://docs.docker.com/get-docker/)
* [Docker Compose](https://docs.docker.com/compose/install/)
* [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
* [Postman](https://www.postman.com/downloads/)

## Features para a fase 3
* API Condutores
* API Veículos
* API Estacionamento
* API Formas de Pagamento

## Coleção para ser importada no POSTMAN: 
```
src/main/resources/FIAP - Parquimetro.postman_collection_v2.json
```

## OpenAPI Swagger:
```
http://localhost:8080/parquimetro/swagger-ui/index.html#/
```

## Para executar o Localstack e o MongoDB, execute o comando:
```shell
docker-compose up -d
```
Verifique se os containers estão rodando com o comando:
```
http://localhost:4566/health
```
retornando:
```json
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

### Cria a fila com o nome "recibos-tempo-estacionamento-sqs" (Executar no console do localstack
```
awslocal sqs create-queue --queue-name "recibos-tempo-estacionamento-sqs"
```

### cria sessão no localstack para o envio de email (Executar no console do localstack)
```
awslocal ses verify-email-identity --email noreply@parquimetro.com.br
```