package org.aditi.api_automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aditi.api_automation.specs.SpecFactory;
import io.restassured.builder.RequestSpecBuilder;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class RequestSpecUtil {
    private RequestSpecUtil() {}

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static RequestSpecBuilder createRequestSpecification(String templatePath, Map<String, Object> dynamicValues) {
        try {
            // Load the resource file as an InputStream
            InputStream inputStream = RequestSpecUtil.class.getClassLoader().getResourceAsStream(templatePath);
            if (inputStream == null) {
                throw new IOException("Resource not found: " + templatePath);
            }

            // Read the InputStream into a String
            String fileContent;
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                // Read the entire InputStream into a single string. The delimiter used is "\\A", which is a regular expression that matches the beginning of the string. This ensures that the entire stream is read into the string, rather than stopping at the first line break.
                fileContent = scanner.useDelimiter("\\A").next();
            }

            // Replace placeholders in the JSON template
            for (Map.Entry<String, Object> entry : dynamicValues.entrySet()) {
                Object value = entry.getValue();
                fileContent = fileContent.replace("\"{{" + entry.getKey() + "}}\"", objectMapper.writeValueAsString(value));
            }

            // Parse the modified JSON template
            JsonNode rootNode = objectMapper.readTree(fileContent);
            JsonNode headersNode = rootNode.path("headers");
            JsonNode bodyNode = rootNode.path("body");
            JsonNode queryParamsNode = rootNode.path("queryParams");
            JsonNode pathParamsNode = rootNode.path("pathParams");

            // Convert nodes to maps
            Map<String, String> headers = objectMapper.convertValue(headersNode, Map.class);
            Map<String, String> queryParams = objectMapper.convertValue(queryParamsNode, Map.class);
            Map<String, String> pathParams = objectMapper.convertValue(pathParamsNode, Map.class);
            String requestBody = objectMapper.writeValueAsString(bodyNode);


            // Create and return the RequestSpecification using RestAssured
            RequestSpecBuilder requestSpecBuilder = SpecFactory.getSpecBuilder();
            if (headers != null && !headers.isEmpty()) {
                requestSpecBuilder.addHeaders(headers);
            }
            if (queryParams != null && !queryParams.isEmpty()) {
                requestSpecBuilder.addQueryParams(queryParams);
            }
            if (requestBody != null && !requestBody.trim().isEmpty()) {
                requestSpecBuilder.setBody(requestBody);
            }
            if(pathParams != null && !pathParams.isEmpty()) {
                requestSpecBuilder.addPathParams(pathParams);
            }
            return requestSpecBuilder;

        } catch (IOException e) {
            log.error("Error reading template file: {}", templatePath, e);
            return null;
        }
    }
}
