package examples.idiomas;

import com.intuit.karate.junit5.Karate;
import com.mycompany.KarateCucumberReporter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class IdiomasRunner {
    
    @BeforeAll
    static void setup() {
        System.out.println("Starting Karate tests with custom Cucumber reporting...");
    }
    
    // Runner para ejecutar la feature de POST idiomas
    @Karate.Test
    Karate testPostIdiomas() {
        return Karate.run("post-idiomas.feature").relativeTo(getClass());
    }

    @Karate.Test
    Karate testGetIdiomas() {
        return Karate.run("get-idiomas.feature").relativeTo(getClass());
    }

    @Karate.Test
    Karate testPutIdiomas() {
        return Karate.run("put-idiomas.feature").relativeTo(getClass());
    }

    @Karate.Test
    Karate testDeleteIdiomas() {
        return Karate.run("delete-idiomas.feature").relativeTo(getClass());
    }
    
    @AfterAll
    static void tearDown() {
        System.out.println("Generating custom Cucumber reports...");
        KarateCucumberReporter.generateReport();
    }
}
