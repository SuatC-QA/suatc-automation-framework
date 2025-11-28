package com.suatc.qa.base;

import com.suatc.qa.config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.*;

public abstract class BaseApiTest {

    @BeforeClass
    public void setUpApi() {
        ConfigReader config = ConfigReader.getInstance();

        baseURI = config.getProperty("url.api");

        String apiKey = config.getProperty("api.key");

        requestSpecification = new RequestSpecBuilder()
                .addHeader("x-api-key", apiKey)
                .build();
    }
}