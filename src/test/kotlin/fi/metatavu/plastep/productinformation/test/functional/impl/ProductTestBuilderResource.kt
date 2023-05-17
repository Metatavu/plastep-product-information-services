package fi.metatavu.plastep.productinformation.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.plastep.productinformation.client.apis.ProductsApi
import fi.metatavu.plastep.productinformation.client.infrastructure.ApiClient
import fi.metatavu.plastep.productinformation.client.infrastructure.ClientException
import fi.metatavu.plastep.productinformation.client.models.Product
import fi.metatavu.plastep.productinformation.client.models.ProductFindResponse
import fi.metatavu.plastep.productinformation.client.models.ProductsListResponse
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
     * Finds product from API
     *
     * @param id id
     * @return found product
     */
    fun find(id: Int): ProductFindResponse {
        return api.findProduct(id = id)
    }

    /**
     * Lists products from API
     *
     * @param page page number. Page number starts from 0
     * @param pageSize page size. Defaults to 10
     * @return product list response
     */
    fun list(page: Int?, pageSize: Int?): ProductsListResponse {
        return api.listProducts(
            page = page,
            pageSize = pageSize
        )
    }

    override fun clean(product: Product) {
        // Products are read-only, so no need to clean them
    }

    /**
     * Asserts that find fails with given status
     *
     * @param expectedStatus expected status
     * @param id id
     */
    fun assertFindFail(expectedStatus: Int, id: Int) {
        try {
            api.findProduct(id)
            fail(String.format("Expected create to fail with status %d", expectedStatus))
        } catch (ex: ClientException) {
            assertClientExceptionStatus(expectedStatus, ex)
        }
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