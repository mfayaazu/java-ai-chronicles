package com.infinotive.ai_issue_investigator.dto;

public record IssueInvestigationRequest(
        String environment,
        String serviceName,
        String issue
) {
}