# Spring AI with OpenAI

This project contains a web service that will accept HTTP GET requests at
- `http://localhost:8080/ai/call`.
  - this endpoint is directly calling the OpenAI ChatGPT Service without any Spring AI implementation.
- `http://localhost:8080/ai/callWithContext`.
  - this endpoint is directly calling the OpenAI ChatGPT Service with a system default response template, documents retrieved from a vector store and stored in memory user conversation historic.

For these two endpoints, there is a `message` parameter which must be supplied in order to use them.

## Prerequisites

Before using the AI commands, make sure you have a developer token from OpenAI.

Create an account at [OpenAI Signup](https://platform.openai.com/signup) and generate the token at [API Keys](https://platform.openai.com/account/api-keys).

The Spring AI project defines a configuration property named `spring.ai.openai.api-key` that you should set to the value of the `API Key` obtained from `openai.com`.

Exporting an environment variable is one way to set that configuration property.
```shell
export SPRING_AI_OPENAI_API_KEY=<INSERT KEY HERE>
```

Setting the API key is all you need to run the application.
However, you can find more information on setting started in the [Spring AI reference documentation section on OpenAI Chat](https://docs.spring.io/spring-ai/reference/api/clients/openai-chat.html).

## Building and running

```
./mvnw spring-boot:run
```

## Access the endpoint

To get a response by using the `message` request parameter
```shell
curl --get  --data-urlencode 'message=Tell me a joke about a cow.' localhost:8080/ai/call 
```
or with context

```shell
curl --get  --data-urlencode 'message=Tell me a joke about a cow.' localhost:8080/ai/callWithContext 
```
A sample response is

```text
Why did the cow go to space?

Because it wanted to see the mooooon!
```

Alternatively use the [httpie](https://httpie.io/) client
```shell
http localhost:8080/ai/callWithContext message=='Tell me a joke about a cow.'
```

## Using chainlit as a chatbot frontend

Chainlit is available in the project at the following location : ``` src/main/resources/front ```
To use it, you need to have Python3 installed in your system.

To install chainlit (prerequisite) :
```shell
pip install chainlit
```

To run chainlit :
```shell
cd src/main/resources/front
chainlit run app.py -w
```

## Using pgvector as a vector store

In order to use pgvector as a vector store, you need to have a PostgreSQL database running with the pgvector extension installed.
For that, I personally recommend using the following docker image : [ankane/pgvector](https://hub.docker.com/r/ankane/pgvector)

For example :
```shell
docker run -it --rm --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres ankane/pgvector
```