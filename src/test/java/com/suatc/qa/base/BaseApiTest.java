package com.suatc.qa.base;

import com.suatc.qa.config.ConfigReader;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {

    @BeforeClass
    public void setUpApi() {
        RestAssured.baseURI = ConfigReader.getInstance().getProperty("url.api");
    }
}