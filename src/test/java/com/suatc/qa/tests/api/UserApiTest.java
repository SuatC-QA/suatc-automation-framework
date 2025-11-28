package com.suatc.qa.tests.api;

import com.suatc.qa.base.BaseApiTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class UserApiTest extends BaseApiTest {

    @Test
    public void shouldReturnUserList() {
        given()
                .queryParam("page", 2)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.size()", greaterThan(0));
    }
}
