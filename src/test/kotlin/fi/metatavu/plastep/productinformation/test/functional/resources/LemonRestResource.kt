package fi.metatavu.plastep.productinformation.test.functional.resources

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.GenericContainer

import com.github.tomakehurst.wiremock.client.WireMock.*
import fi.metatavu.plastep.lemon.client.models.*
import org.slf4j.LoggerFactory

/**
 * Resource for Lemon mock service container
 */
class LemonRestResource : QuarkusTestResourceLifecycleManager {

    override fun start(): Map<String, String> {
        container.start()

        val host = container.host
        val port = container.getMappedPort(8080)

        configureFor(host, port)

        createLoginStubs()
        createProductStubs()

        return mapOf(
            "lemon.rest.url" to "http://$host:$port",
            "lemon.rest.database" to DATABASE,
            "lemon.rest.apikey" to APIKEY,
            "lemon.rest.username" to USERNAME,
            "lemon.rest.password" to PASSWORD
        )
    }

    /**
     * Create stubs for Lemon login endpoints
     */
    private fun createLoginStubs() {
        val objectMapper = jacksonObjectMapper()

        stubFor(
            post(urlPathEqualTo("/api/auth/login"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(
                    LoginPayload(
                        apiKey = APIKEY,
                        database = DATABASE,
                        userName = USERNAME,
                        password = PASSWORD
                    )
                )))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(
                    LoginResult(
                        sessionId = SESSION_ID,
                        code = 200,
                        message = "ok",
                        version = "1.2.3"
                    )
                ), 200))
        )
    }

    /**
     * Creates stubs for Lemon product endpoints
     */
    private fun createProductStubs() {
        val objectMapper = jacksonObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        val products = (1..4)
            .map { "/lemon/test-product-${it}.json" }
            .map { objectMapper.readValue<Product>(this.javaClass.getResource(it)!!) }

        val productStructures = (1..4)
            .map { "/lemon/test-product-${it}-structure.json" }
            .map { objectMapper.readValue<GetProductStructureResult>(this.javaClass.getResource(it)!!) }

        products.forEachIndexed { index, product ->
            stubFor(
                get(urlPathEqualTo("/api/products/${product.id}"))
                    .willReturn(jsonResponse(objectMapper.writeValueAsString(GetProductResult(
                        result = product
                    )), 200))
            )

            stubFor(
                get(urlPathEqualTo("/api/products/${product.sku}/0/1"))
                    .willReturn(jsonResponse(objectMapper.writeValueAsString(productStructures[index]), 200))
            )
        }

        stubFor(
            get(urlPathEqualTo("/api/products"))
                .withQueryParams(mapOf(
                    "filter.page" to equalTo("1"),
                    "filter.page_size" to equalTo("10")
                ))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(ProductListResult(
                    results = products.toTypedArray()
                )), 200))
        )

        stubFor(
            get(urlPathEqualTo("/api/products"))
                .withQueryParams(mapOf(
                    "filter.page" to equalTo("2"),
                    "filter.page_size" to equalTo("2")
                ))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(ProductListResult(
                    results = products.subList(1, 3).toTypedArray()
                )), 200))
        )

        stubFor(
            get(urlPathEqualTo("/api/products"))
                .withQueryParams(mapOf(
                    "filter.page" to equalTo("3"),
                    "filter.page_size" to equalTo("2")
                ))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(ProductListResult(
                    results = products.subList(2, 4).toTypedArray()
                )), 200))
        )

        stubFor(
            get(urlPathEqualTo("/api/products"))
                .withQueryParams(mapOf(
                    "filter.page_size" to equalTo("0")
                ))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(ProductListResult(
                    results = arrayOf()
                )), 200))
        )
    }

    override fun stop() {
        container.stop()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LemonRestResource::class.java)

        var container: GenericContainer<*> = GenericContainer("rodolpheche/wiremock")
            .withLogConsumer {
                logger.info(it.utf8String)
            }
            .withExposedPorts(8080)

        const val DATABASE = "test"
        const val APIKEY = "testkey"
        const val USERNAME = "testuser"
        const val PASSWORD = "testpass"
        const val SESSION_ID = "test-session-id"
    }

}