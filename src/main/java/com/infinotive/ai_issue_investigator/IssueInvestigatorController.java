package com.infinotive.ai_issue_investigator;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
public class IssueInvestigatorController {

    private final ChatClient chatClient;

    public IssueInvestigatorController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("/investigate")
    public String investigate(@RequestBody String issueText) {
        return chatClient.prompt()
                .user("""
                         You are a senior Java backend engineer with expertise in:
                         - Spring Boot
                         - ActiveMQ
                         - Kubernetes
                         - AWS
                         - production incident troubleshooting
                        
                         Analyze the following production issue.
                        
                         Give the answer in this exact format:
                        
                         ## Incident Summary
                         Explain the issue in simple terms.
                        
                         ## Most Likely Root Causes
                         Give 3-5 realistic causes.
                        
                         ## Immediate Checks
                         Give practical checks an engineer should do now.
                        
                         ## Commands / Logs to Check
                        Suggest only safe and commonly used checks.
                        If the exact command depends on ActiveMQ Classic, ActiveMQ Artemis, Kubernetes, or cloud setup, clearly say that it depends on the environment.
                        Prefer practical checks like Kubernetes pod logs, broker UI, JMX/Jolokia metrics, consumer logs, queue depth, retry count, and poison message details.
                        Do not invent command names
                        
                         ## Suggested Fix
                         Give short-term and long-term fix.
                        
                         ## Risk if Ignored
                         Explain what can happen if this continues.
                        
                         Production Issue:
                        """ + issueText)
                .call()
                .content();
    }
}