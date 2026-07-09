# The Java × AI Chronicles

## Episode 1: AI Production Issue Investigator

This is my first local AI-powered Java application.

## Tech Stack

- Java 21
- Spring Boot
- Spring AI
- Ollama
- Llama 3.2

## What it does

The application exposes a REST endpoint where a production issue can be submitted.

It analyzes the issue and returns:

- Incident summary
- Possible root causes
- Immediate checks
- Logs or dashboards to verify
- Suggested fixes
- Risk if ignored

## Key Learning

AI can assist engineers, but engineers still need to validate the output.

The model gave useful troubleshooting direction, but some operational details still need engineering judgement.