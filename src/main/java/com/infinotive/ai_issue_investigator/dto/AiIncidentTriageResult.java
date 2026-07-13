package com.infinotive.ai_issue_investigator.dto;

import java.util.List;

public record AiIncidentTriageResult(
        String severity,
        int priorityScore,
        String category,
        String environment,
        String serviceName,
        String probableOwnerTeam,
        boolean shouldEscalate,
        String escalationReason,
        List<String> suspectedCauses,
        List<String> immediateActions,
        String businessImpact,
        int confidenceScore,
        String reasoning
) {
}