# The Java × AI Chronicles

A build-in-public journey exploring how Java, Spring Boot, Spring AI, and local LLMs can be used to build enterprise-style AI applications.

This project is an **AI Production Issue Investigator**. It helps analyze production-style issues and convert messy incident descriptions into structured engineering insight.

---

## Tech Stack

- Java 21
- Spring Boot
- Spring AI
- Ollama
- Llama 3.2
- Maven
- REST API

---

## Why This Project Exists

Modern enterprise systems generate a lot of operational noise: logs, incidents, queue failures, service issues, deployment problems, and production alerts.

The goal of this project is to explore how AI can assist backend engineers by:

- Summarizing production issues
- Suggesting likely root causes
- Recommending immediate checks
- Supporting incident triage
- Converting unstructured issue descriptions into structured decision support

AI does not replace engineering judgement. It supports engineers by helping them reason faster and more consistently.

---

## Local Setup

### 1. Start Ollama

```bash
ollama serve
```

### 2. Run Llama 3.2

```bash
ollama run llama3.2
```

### 3. Start the Spring Boot Application

```bash
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```

---

# Episode 1: Local AI-Powered Production Issue Investigator

## Goal

Build the first working version of a Java application that can talk to a local LLM using Spring AI and Ollama.

## What Was Built

A REST endpoint that accepts a production-style issue as plain text and returns an AI-generated investigation summary.

## Endpoint

```http
POST /api/issues/investigate
```

## Request Example

```bash
curl -s -X POST http://localhost:8080/api/issues/investigate \
  -H "Content-Type: text/plain" \
  -d "ActiveMQ DLQ is increasing continuously. We clear the queue but messages keep coming back. No obvious error in application logs."
```

## Response Includes

- Incident summary
- Most likely root causes
- Immediate checks
- Commands, logs, or dashboards to verify
- Suggested fix
- Risk if ignored

## Key Learning

AI can assist engineers with investigation direction, but engineers must still validate the output before trusting it in production.

The first version worked, but it also showed that AI responses can be too generic or include unsafe assumptions if the prompt is not controlled properly.

---

# Episode 2: From Demo Code to Enterprise-Style Service

## Goal

Refactor the application from a simple demo into a cleaner Spring Boot structure.

## What Was Improved

The first version had AI logic directly inside the controller. In Episode 2, the application was refactored into a cleaner flow:

```text
Controller → Service → Spring AI / Ollama
```

## Improvements Added

- Created request and response DTOs
- Moved AI logic into a service layer
- Improved prompt control
- Reduced risk of fake or environment-specific command suggestions
- Made the API accept structured JSON input

## Endpoint

```http
POST /api/issues/investigate
```

## Request Example

```bash
curl -s -X POST http://localhost:8080/api/issues/investigate \
  -H "Content-Type: application/json" \
  -d '{
    "environment": "QA",
    "serviceName": "activemq-consumer-service",
    "issue": "ActiveMQ DLQ is increasing continuously. We clear the queue but messages keep coming back. No obvious error in application logs."
  }' | jq -r '.investigation' | glow
```

## Key Learning

Working AI code is not enough. Enterprise AI applications need clean structure, clear responsibilities, and safer prompt control.

The system prompt was improved to guide the model away from inventing unknown commands and toward safer production checks such as:

- Broker UI
- Consumer logs
- DLQ message inspection
- Retry count
- Poison message headers
- JMX / Jolokia metrics
- Kubernetes logs where applicable

---

# Episode 3: AI Incident Decision Engine

## Goal

Move beyond generating investigation text and make the AI return a structured incident decision.

## What Was Built

A new triage endpoint that converts an unstructured production issue into structured decision support.

Instead of only returning a long text answer, the application now classifies the issue and returns fields such as severity, priority, owner team, escalation decision, and reasoning.

## Endpoint

```http
POST /api/issues/triage
```

## Request Example

```bash
curl -s -X POST http://localhost:8080/api/issues/triage \
  -H "Content-Type: application/json" \
  -d '{
    "environment": "QA",
    "serviceName": "activemq-consumer-service",
    "issue": "ActiveMQ DLQ is increasing continuously. We clear the queue but messages keep coming back. No obvious error in application logs."
  }' | jq
```

## Response Fields

The AI returns a structured incident decision including:

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

## Example Response Shape

```json
{
  "severity": "HIGH",
  "priorityScore": 8,
  "category": "Messaging",
  "environment": "QA",
  "serviceName": "activemq-consumer-service",
  "probableOwnerTeam": "Backend / Integration Team",
  "shouldEscalate": true,
  "escalationReason": "Repeated DLQ growth may indicate delayed processing, poison messages, or repeated operational intervention.",
  "suspectedCauses": [
    "Poison message",
    "Consumer processing failure",
    "Retry policy issue"
  ],
  "immediateActions": [
    "Inspect DLQ message details",
    "Check consumer logs",
    "Verify retry and acknowledgement behavior"
  ],
  "businessImpact": "Delayed message processing and possible downstream impact if unresolved.",
  "confidenceScore": 78,
  "reasoning": "The issue is recurring after manual clearing, which suggests a repeated processing failure rather than a one-time queue backlog."
}
```

## Key Learning

AI is not only useful for generating text.

It can also help transform messy, human-written production issue descriptions into structured engineering decisions.

Normal Java code can validate fields and route requests, but AI can understand unstructured operational context and support triage decisions.

The engineer still owns the final judgement.

---

## Current Project Flow

```text
User / Engineer
      ↓
Spring Boot REST API
      ↓
Controller
      ↓
Service Layer
      ↓
Spring AI ChatClient
      ↓
Ollama
      ↓
Llama 3.2
      ↓
Investigation or Structured Incident Decision
```

---

## Lessons So Far

1. A local LLM can be integrated into a Java application without paid APIs.
2. Prompt design strongly affects the quality and safety of AI output.
3. AI output must be validated by engineers.
4. Clean Spring Boot architecture still matters in AI applications.
5. Structured AI output is more useful than plain text for enterprise workflows.
6. AI works best as decision support, not as an unchecked decision maker.

---

## Next Episode

Episode 4 will focus on improving production readiness by adding validation, error handling, and cleaner API responses.

