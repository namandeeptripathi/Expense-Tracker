package com.namandeep.expensetracker.controller;

import com.namandeep.expensetracker.dto.ReportResponse;
import com.namandeep.expensetracker.service.ReportService;
import com.namandeep.expensetracker.util.ReportCsvExporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Authenticated financial summary endpoints for the current user. */
@RestController
@Validated
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "User-owned financial summaries")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    @Operation(
            summary = "Generate financial report",
            description = "Returns aggregated income, expense, net savings, and transaction totals for the current user.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Report generated",
                    content = @Content(schema = @Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ReportResponse generateReport(Authentication authentication) {
        return reportService.generateReport(authentication.getName());
    }

    @GetMapping(value = "/export/csv", produces = "text/csv")
    @Operation(
            summary = "Export financial report as CSV",
            description = "Downloads aggregated income, expense, net savings, and transaction totals as a CSV file.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "CSV file returned",
                    content = @Content(mediaType = "text/csv")),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<Resource> exportCsv(Authentication authentication) {
        ReportResponse report = reportService.generateReport(authentication.getName());
        ByteArrayResource resource = new ByteArrayResource(ReportCsvExporter.toCsv(report));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"financial-report.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }
}
