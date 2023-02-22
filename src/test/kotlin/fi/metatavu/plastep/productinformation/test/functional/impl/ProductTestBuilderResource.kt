package fi.metatavu.plastep.productinformation.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.plastep.productinformation.client.apis.ProductsApi
import fi.metatavu.plastep.productinformation.client.infrastructure.ApiClient
import fi.metatavu.plastep.productinformation.client.infrastructure.ClientException
import fi.metatavu.plastep.productinformation.client.models.Product
import fi.metatavu.plastep.productinformation.test.functional.TestBuilder
import fi.metatavu.plastep.productinformation.test.functional.settings.ApiTestSettings
import org.junit.jupiter.api.fail

/**
 * Test Builder resource for Product API
 *
 * @author Antti Leppä
 */
class ProductTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
): ApiTestBuilderResource<Product, ApiClient?>(testBuilder, apiClient) {

    override fun getApi(): ProductsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return ProductsApi(ApiTestSettings.apiBasePath)
    }

    /**
     * Lists products from API
     *
     * @param page page number. Page number starts from 0
     * @param pageSize page size. Defaults to 10
     */
    fun list(page: Int?, pageSize: Int?): Array<Product> {
        return api.listProducts(
            page = page,
            pageSize = pageSize
        )
    }

    override fun clean(product: Product) {
        // Products are read-only, so no need to clean them
    }

    /**
     * Asserts that list fails with given status
     *
     * @param expectedStatus expected status
     * @param page page number
     * @param pageSize page size
     */
    fun assertListFail(expectedStatus: Int, page: Int? = null, pageSize: Int? = null) {
        try {
            api.listProducts(page = page, pageSize = pageSize)
            fail(String.format("Expected create to fail with status %d", expectedStatus))
        } catch (ex: ClientException) {
            assertClientExceptionStatus(expectedStatus, ex)
        }
    }

}