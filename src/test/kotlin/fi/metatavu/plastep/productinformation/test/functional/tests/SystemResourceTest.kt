package fi.metatavu.plastep.productinformation.test.functional.tests

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test

/**
 * Tests for system resources
 *
 * @author Antti Leppä
 */
@QuarkusTest
class SystemResourceTest : AbstractResourceTest() {

    @Test
    fun testPingEndpoint() {
        given()
            .contentType("application/json")
            .`when`().get("/v1/system/ping")
            .then()
            .statusCode(200)
            .body(`is`("pong"))
    }

}