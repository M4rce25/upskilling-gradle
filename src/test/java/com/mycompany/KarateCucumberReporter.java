package com.mycompany;

import com.intuit.karate.core.Feature;
import com.intuit.karate.core.Result;
import com.intuit.karate.core.Scenario;
import com.intuit.karate.core.Step;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class KarateCucumberReporter implements TestExecutionListener {
    
    private static final List<ScenarioResult> scenarioResults = new ArrayList<>();
    private static final String REPORT_DIR = "build/cucumber-reports";
    
    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED) {
            testExecutionResult.getThrowable().ifPresent(throwable -> {
                System.out.println("Test failed: " + testIdentifier.getDisplayName());
                System.out.println("Error: " + throwable.getMessage());
            });
        }
    }
    
    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        generateReport();
    }
    
    public static void addScenarioResult(String featureName, String scenarioName, 
                                     String status, long durationMs, List<String> steps) {
        scenarioResults.add(new ScenarioResult(featureName, scenarioName, status, durationMs, steps));
    }
    
    public static void generateReport() {
        try {
            // Crear directorio de reportes
            File reportDir = new File(REPORT_DIR);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }
            
            // Generar JSON report
            generateJsonReport();
            
            // Generar HTML report
            generateHtmlReport();
            
            System.out.println("Cucumber-style reports generated in: " + REPORT_DIR);
            
        } catch (Exception e) {
            System.err.println("Error generating reports: " + e.getMessage());
        }
    }
    
    private static void generateJsonReport() throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < scenarioResults.size(); i++) {
            ScenarioResult result = scenarioResults.get(i);
            json.append("  {\n");
            json.append("    \"keyword\": \"Feature\",\n");
            json.append("    \"name\": \"").append(result.featureName).append("\",\n");
            json.append("    \"uri\": \"").append(result.featureName.replace(" ", "-")).append(".feature\",\n");
            json.append("    \"elements\": [\n");
            json.append("      {\n");
            json.append("        \"keyword\": \"Scenario\",\n");
            json.append("        \"name\": \"").append(result.scenarioName).append("\",\n");
            json.append("        \"line\": 1,\n");
            json.append("        \"type\": \"scenario\",\n");
            json.append("        \"steps\": [\n");
            
            for (int j = 0; j < result.steps.size(); j++) {
                String step = result.steps.get(j);
                json.append("          {\n");
                json.append("            \"keyword\": \"Given\",\n");
                json.append("            \"name\": \"").append(step).append("\",\n");
                json.append("            \"line\": ").append(j + 1).append(",\n");
                json.append("            \"result\": {\n");
                json.append("              \"status\": \"").append(result.status).append("\",\n");
                json.append("              \"duration\": ").append(result.durationMs * 1000000).append("\n");
                json.append("            }\n");
                json.append("          }");
                if (j < result.steps.size() - 1) json.append(",");
                json.append("\n");
            }
            
            json.append("        ]\n");
            json.append("      }\n");
            json.append("    ]\n");
            json.append("  }");
            if (i < scenarioResults.size() - 1) json.append(",");
            json.append("\n");
        }
        
        json.append("]\n");
        
        try (FileWriter writer = new FileWriter(REPORT_DIR + "/cucumber.json")) {
            writer.write(json.toString());
        }
    }
    
    private static void generateHtmlReport() throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <title>Karate Cucumber Report</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append("        .header { background-color: #2c3e50; color: white; padding: 20px; border-radius: 5px; }\n");
        html.append("        .scenario { border: 1px solid #ddd; margin: 10px 0; padding: 15px; border-radius: 5px; }\n");
        html.append("        .passed { border-left: 5px solid #27ae60; }\n");
        html.append("        .failed { border-left: 5px solid #e74c3c; }\n");
        html.append("        .feature-name { font-weight: bold; color: #2c3e50; }\n");
        html.append("        .scenario-name { font-size: 1.1em; margin: 10px 0; }\n");
        html.append("        .steps { margin-left: 20px; }\n");
        html.append("        .step { margin: 5px 0; }\n");
        html.append("        .status { padding: 2px 8px; border-radius: 3px; color: white; font-size: 0.8em; }\n");
        html.append("        .status.passed { background-color: #27ae60; }\n");
        html.append("        .status.failed { background-color: #e74c3c; }\n");
        html.append("        .summary { background-color: #ecf0f1; padding: 15px; border-radius: 5px; margin: 20px 0; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        
        // Header
        html.append("    <div class=\"header\">\n");
        html.append("        <h1>Karate API Test Report</h1>\n");
        html.append("        <p>Generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("</p>\n");
        html.append("    </div>\n");
        
        // Summary
        long passed = scenarioResults.stream().filter(r -> "passed".equals(r.status)).count();
        long failed = scenarioResults.stream().filter(r -> "failed".equals(r.status)).count();
        long total = scenarioResults.size();
        
        html.append("    <div class=\"summary\">\n");
        html.append("        <h2>Summary</h2>\n");
        html.append("        <p>Total Scenarios: ").append(total).append("</p>\n");
        html.append("        <p>Passed: <span class=\"status passed\">").append(passed).append("</span></p>\n");
        html.append("        <p>Failed: <span class=\"status failed\">").append(failed).append("</span></p>\n");
        html.append("        <p>Success Rate: ").append(total > 0 ? (passed * 100 / total) : 0).append("%</p>\n");
        html.append("    </div>\n");
        
        // Scenarios
        html.append("    <h2>Scenarios</h2>\n");
        
        for (ScenarioResult result : scenarioResults) {
            String cssClass = result.status.equals("passed") ? "passed" : "failed";
            html.append("    <div class=\"scenario ").append(cssClass).append("\">\n");
            html.append("        <div class=\"feature-name\">Feature: ").append(result.featureName).append("</div>\n");
            html.append("        <div class=\"scenario-name\">Scenario: ").append(result.scenarioName).append("</div>\n");
            html.append("        <div class=\"status ").append(result.status).append("\">").append(result.status.toUpperCase()).append("</div>\n");
            html.append("        <div class=\"steps\">\n");
            
            for (String step : result.steps) {
                html.append("            <div class=\"step\">• ").append(step).append("</div>\n");
            }
            
            html.append("        </div>\n");
            html.append("    </div>\n");
        }
        
        html.append("</body>\n");
        html.append("</html>\n");
        
        try (FileWriter writer = new FileWriter(REPORT_DIR + "/cucumber-report.html")) {
            writer.write(html.toString());
        }
    }
    
    private static class ScenarioResult {
        String featureName;
        String scenarioName;
        String status;
        long durationMs;
        List<String> steps;
        
        ScenarioResult(String featureName, String scenarioName, String status, long durationMs, List<String> steps) {
            this.featureName = featureName;
            this.scenarioName = scenarioName;
            this.status = status;
            this.durationMs = durationMs;
            this.steps = steps;
        }
    }
}
