package com.infinotive.ai_issue_investigator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IssueInvestigationRequest(

        @NotBlank(message = "Environment is required")
        @Size(max = 50, message = "Environment must be less than 50 characters")
        String environment,

        @NotBlank(message = "Service name is required")
        @Size(max = 100, message = "Service name must be less than 100 characters")
        String serviceName,

        @NotBlank(message = "Issue description is required")
        @Size(min = 20, max = 4000, message = "Issue description must be between 20 and 4000 characters")
        String issue
) {
}