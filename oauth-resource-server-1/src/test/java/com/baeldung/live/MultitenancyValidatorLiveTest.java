package com.baeldung.live;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class MultitenancyValidatorLiveTest {

    @Test
    public void whenUsingUserWithAllowedDomain_thenCorrect() {
        final Response authServerResponse = obtainAccessToken("fooClientIdPassword", "saulgoodman@baeldung.com", "789");
        final String accessToken = authServerResponse.jsonPath().getString("access_token");
        assertNotNull(accessToken);

        final Response resourceServerResponse = RestAssured.given().header("Authorization", "Bearer " + accessToken).get("http://localhost:8082/spring-security-oauth-resource/multitenancy/foos/100");
        assertThat(resourceServerResponse.getStatusCode(), equalTo(200));
    }

    @Test
    public void whenUsingUserWithRejectedDomain_thenNotOk() {
        final Response authServerResponse = obtainAccessToken("fooClientIdPassword", "michaelehrmantraut@test.com", "987");
        final String accessToken = authServerResponse.jsonPath().getString("access_token");
        assertNotNull(accessToken);

        final Response resourceServerResponse = RestAssured.given().header("Authorization", "Bearer " + accessToken).get("http://localhost:8082/spring-security-oauth-resource/multitenancy/foos/100");
        assertThat(resourceServerResponse.getStatusCode(), equalTo(401));
    }

    private Response obtainAccessToken(String clientId, String username, String password) {
        final Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        return RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when().post("http://localhost:8081/spring-security-oauth-server/oauth/token");
    }


}
