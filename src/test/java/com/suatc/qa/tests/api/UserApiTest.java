package com.suatc.qa.tests.api;

import com.suatc.qa.base.BaseApiTest;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


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

    @Test
    public void shouldCreateUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"morpheus\",\"job\":\"leader\"}")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @Test
    public void shouldReturn404ForUnknownUser() {
        given()
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404);
    }

}
