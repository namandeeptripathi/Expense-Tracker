package com.namandeep.expensetracker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Typed configuration used to describe the public API. */
@ConfigurationProperties(prefix = "expense-tracker.api")
public record ApiProperties(String title, String description, String version) {
}
