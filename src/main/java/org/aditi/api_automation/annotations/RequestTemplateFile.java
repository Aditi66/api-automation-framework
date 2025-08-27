package org.aditi.api_automation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestTemplateFile {
    /**
     * The value should be a path to a JSON file containing the request details.
     * This will be used to generate a RestAssured request specification.
     */
    String value();
}
