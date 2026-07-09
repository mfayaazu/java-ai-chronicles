package com.infinotive.ai_issue_investigator;

import com.infinotive.ai_issue_investigator.dto.IssueInvestigationRequest;
import com.infinotive.ai_issue_investigator.dto.IssueInvestigationResponse;
import com.infinotive.ai_issue_investigator.service.IssueInvestigationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
public class IssueInvestigatorController {

    private final IssueInvestigationService issueInvestigationService;

    public IssueInvestigatorController(IssueInvestigationService issueInvestigationService) {
        this.issueInvestigationService = issueInvestigationService;
    }

    @PostMapping("/investigate")
    public IssueInvestigationResponse investigate(@RequestBody IssueInvestigationRequest request) {
        return issueInvestigationService.investigate(request);
    }
}