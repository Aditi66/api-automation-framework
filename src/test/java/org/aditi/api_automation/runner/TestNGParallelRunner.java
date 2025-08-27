package org.aditi.api_automation.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features", // Path to your feature files
        glue = {
                "org.aditi.api_automation.steps"
        },     // Package containing step definitions
        publish = true,
//        tags = "@Parallel" ,                       // Tags to filter scenarios
        plugin = {"pretty", "json:target/cucumber.json"} // Plugins for reporting
)
public class TestNGParallelRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true) // Enable parallel execution
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
