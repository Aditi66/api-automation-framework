package org.aditi.api_automation.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "org.aditi.api_automation.steps",
        },
//        tags = "@ItemGroupRegression",
        monochrome = true,
        publish = true,
        plugin = {
                "pretty",
                "json:target/cucumber.json"
        })
public class CucumberTest {
}
