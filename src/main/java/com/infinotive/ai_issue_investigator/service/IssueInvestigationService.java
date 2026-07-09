package com.infinotive.ai_issue_investigator.service;

import com.infinotive.ai_issue_investigator.dto.IssueInvestigationRequest;
import com.infinotive.ai_issue_investigator.dto.IssueInvestigationResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class IssueInvestigationService {

    private final ChatClient chatClient;

    public IssueInvestigationService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public IssueInvestigationResponse investigate(IssueInvestigationRequest request) {
        String response = chatClient.prompt()
                .system("""
                You are a senior Java production support engineer.

                Important rules:
                - Do not invent command names.
                - Do not suggest tools unless they are common and safe.
                - If an exact command depends on the environment, say that clearly.
                - Prefer general checks over fake-specific commands.
                - For Kubernetes, kubectl logs and kubectl describe pod are allowed.
                - For ActiveMQ, suggest broker UI, DLQ message inspection, consumer logs, retry count, poison message headers, JMX/Jolokia metrics.
                """)
                .user(buildPrompt(request))
                .call()
                .content();

        return new IssueInvestigationResponse(response);
    }

    private String buildPrompt(IssueInvestigationRequest request) {
        return """
                You are a senior Java backend engineer with expertise in:
                - Spring Boot
                - ActiveMQ
                - Kubernetes
                - AWS
                - production incident troubleshooting

                Analyze the following production issue.

                Environment: %s
                Service Name: %s
                Issue: %s

                Give the answer in this exact format:

                ## Incident Summary
                Explain the issue in simple terms.

                ## Most Likely Root Causes
                Give 3-5 realistic causes.

                ## Immediate Checks
                Give practical checks an engineer should do now.

                ## Commands / Logs to Check
                Suggest safe checks only.
                Do not provide unknown CLI commands for ActiveMQ.
                Use bullet points without fake command names.

                ## Suggested Fix
                Give short-term and long-term fix.

                ## Risk if Ignored
                Explain what can happen if this continues.
                """.formatted(
                request.environment(),
                request.serviceName(),
                request.issue()
        );
    }
}