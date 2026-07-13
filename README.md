# The Java × AI Chronicles

A build-in-public learning project focused on **Enterprise AI Engineering with Java**.

This repository contains a local AI-powered production issue investigation application built with:

- Java 21
- Spring Boot
- Spring AI
- Ollama
- Llama 3.2
- REST APIs

The goal is not to build a generic chatbot.

The goal is to explore how AI can assist real enterprise engineering workflows such as production incident investigation, triage, escalation support, and decision support — while still keeping Java engineering fundamentals in control.

---

## Project: AI Production Issue Investigator

This application exposes REST APIs that accept production-style issue descriptions and use a local LLM to assist with investigation and triage.

The application runs locally using Ollama, so no paid API is required.

---

## Architecture

```text
Client / Terminal
      ↓
Spring Boot REST Controller
      ↓
Validation & API Contract
      ↓
Issue Investigation Service
      ↓
Java Guardrail Logic
      ↓
Spring AI
      ↓
Ollama / Llama 3.2
```

Important design idea:

```text
AI assists with understanding messy incident descriptions.
Java controls validation, contracts, guardrails, and final safety decisions.
```

---

# Episodes

## Episode 1: Local AI-Powered Java Application

The first episode focused on proving the basic integration.

### Goal

Build a Spring Boot application that can send a production issue to a local LLM and receive an investigation response.

### What was built

A REST endpoint that accepts a production issue and returns:

- Incident summary
- Most likely root causes
- Immediate checks
- Logs or dashboards to verify
- Suggested fix
- Risk if ignored

### Key learning

AI can help engineers generate investigation direction, but the engineer still needs to validate the output.

The first version worked, but the AI response could still suggest details that may not apply to every environment. That was the first lesson in prompt control and engineering judgement.

---

## Episode 2: From Demo Code to Enterprise-Style Service

Episode 2 improved the structure of the application.

### Goal

Move from a simple demo-style controller into a cleaner Spring Boot architecture.

### What changed

The flow was refactored into:

```text
Controller → Service → Spring AI / Ollama
```

Request and response DTOs were introduced.

### Example request

```json
{
  "environment": "QA",
  "serviceName": "activemq-consumer-service",
  "issue": "ActiveMQ DLQ is increasing continuously. We clear the queue but messages keep coming back. No obvious error in application logs."
}
```

### Key learning

Working AI code is not enough.

Enterprise applications still need clean layering, clear API contracts, maintainable services, and controlled prompts.

---

## Episode 3: AI Incident Decision Engine

Episode 3 made the use case more AI-driven.

### Goal

Instead of only generating a long investigation text, the application now converts an unstructured production issue into a structured incident decision.

### Endpoint

```http
POST /api/issues/triage
```

### What the AI returns

The triage response includes:

- Severity
- Priority score
- Category
- Environment
- Service name
- Probable owner team
- Escalation decision
- Escalation reason
- Suspected causes
- Immediate actions
- Business impact
- Confidence score
- Reasoning

### Why AI is useful here

Normal Java code can validate fields and route requests.

But AI can understand messy, human-written production issue descriptions and convert them into structured decision support.

### Key learning

AI is not only useful for generating text.

It can also help transform unstructured operational information into structured engineering decisions.

The engineer still owns the final judgement.

---

## Episode 4: AI Needs Engineering Guardrails

Episode 4 focuses on an important enterprise AI lesson:

```text
AI is powerful, but AI alone is not enough for production systems.
```

### Goal

Add production-readiness and Java-based guardrails around the AI incident triage flow.

### What was added

- Request validation
- Clean API error responses
- Global exception handling
- Java-based guardrail decision
- Human review requirement
- Final decision source
- Applied guardrails list

### Why this matters

Not every request should reach AI.

Some problems are better handled deterministically by Java validation and API contracts.

AI should assist when it adds value, but Java should still control:

- Validation
- API contracts
- Error handling
- Safety rules
- Guardrail decisions
- Human review requirements

---

## Episode 4 Demo Scenarios

### Scenario 1: Invalid request

Request:

```json
{
  "environment": "",
  "serviceName": "",
  "issue": "short"
}
```

Expected result:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "validationErrors": {
    "environment": "Environment is required",
    "serviceName": "Service name is required",
    "issue": "Issue description must be between 20 and 4000 characters"
  }
}
```

### Lesson

This request should not go to AI.

Java validation rejects it first.

---

### Scenario 2: Valid production issue

Request:

```json
{
  "environment": "QA",
  "serviceName": "activemq-consumer-service",
  "issue": "ActiveMQ DLQ is increasing continuously. We clear the queue but messages keep coming back. No obvious error in application logs."
}
```

The response includes AI-supported triage plus Java guardrail fields:

```json
{
  "severity": "HIGH",
  "priorityScore": 8,
  "shouldEscalate": true,
  "guardrailDecision": "ESCALATE",
  "humanReviewRequired": true,
  "finalDecisionSource": "JAVA_GUARDRAIL_SERVICE",
  "aiGenerated": true
}
```

### Lesson

AI helps classify the incident.

Java applies the final guardrail decision and decides whether human review is required.

---

# API Endpoints

## Investigation Endpoint

```http
POST /api/issues/investigate
```

Returns a detailed investigation-style response.

## Triage Endpoint

```http
POST /api/issues/triage
```

Returns a structured incident decision.

---

# Running Locally

## 1. Start Ollama

```bash
ollama serve
```

## 2. Pull and run the model

```bash
ollama run llama3.2
```

## 3. Start the Spring Boot application

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

# Example Commands

## Invalid request test

```bash
curl -s -X POST http://localhost:8080/api/issues/triage \
  -H "Content-Type: application/json" \
  -d '{
    "environment": "",
    "serviceName": "",
    "issue": "short"
  }' | jq
```

## Valid triage test

```bash
curl -s -X POST http://localhost:8080/api/issues/triage \
  -H "Content-Type: application/json" \
  -d '{
    "environment": "QA",
    "serviceName": "activemq-consumer-service",
    "issue": "ActiveMQ DLQ is increasing continuously. We clear the queue but messages keep coming back. No obvious error in application logs."
  }' | jq
```

## Investigation test with formatted Markdown output

```bash
curl -s -X POST http://localhost:8080/api/issues/investigate \
  -H "Content-Type: application/json" \
  -d '{
    "environment": "QA",
    "serviceName": "activemq-consumer-service",
    "issue": "ActiveMQ DLQ is increasing continuously. We clear the queue but messages keep coming back. No obvious error in application logs."
  }' | jq -r '.investigation' | glow
```

---

# Key Learnings So Far

## Episode 1

AI can help generate troubleshooting direction, but engineers must validate the output.

## Episode 2

A working AI demo still needs clean Spring Boot architecture.

## Episode 3

AI can convert messy production issue descriptions into structured decision support.

## Episode 4

AI needs engineering guardrails.

AI can assist engineers, but enterprise systems still require validation, contracts, error handling, safety rules, and human judgement.

---

# Main Message

This project is based on one principle:

```text
AI is not replacing engineering fundamentals.
AI is making strong engineering even more important.
```

Java developers are still needed to design, validate, secure, control, and operate AI-powered enterprise systems.

AI can help with reasoning, classification, summarization, and decision support.

But production systems still need strong software engineering.
