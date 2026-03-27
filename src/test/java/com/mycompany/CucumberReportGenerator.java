package com.mycompany;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CucumberReportGenerator {
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: CucumberReportGenerator <json-report-path> <output-directory>");
            System.exit(1);
        }
        
        String jsonReportPath = args[0];
        String outputDirectory = args[1];
        
        try {
            generateReports(jsonReportPath, outputDirectory);
            System.out.println("Cucumber reports generated successfully!");
            System.out.println("Open file://" + new File(new File(outputDirectory, "cucumber-html-reports"), "overview-features.html").getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error generating reports: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static void generateReports(String jsonReportPath, String outputDirectory) throws Exception {
        File reportOutputDirectory = new File(outputDirectory);
        File jsonFile = new File(jsonReportPath);
        
        if (!jsonFile.exists()) {
            throw new RuntimeException("JSON report file not found: " + jsonReportPath);
        }
        
        // Crear configuración
        Configuration configuration = new Configuration(reportOutputDirectory, "Karate API Tests");
        configuration.setBuildNumber("1.0");
        
        // Agregar archivos JSON
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add(jsonFile.getAbsolutePath());
        
        // Generar reporte
        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        Reportable result = reportBuilder.generateReports();
        
        System.out.println("Report generated successfully!");
        System.out.println("Total scenarios processed: " + result.getScenarios());
    }
}
