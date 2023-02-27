package fi.metatavu.plastep.productinformation.lemon

import fi.metatavu.plastep.lemon.client.apis.AuthenticationApi
import fi.metatavu.plastep.lemon.client.apis.ProductApi
import fi.metatavu.plastep.lemon.client.infrastructure.ApiClient
import fi.metatavu.plastep.lemon.client.models.*
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.annotation.PostConstruct
import javax.enterprise.context.RequestScoped

/**
 * Client for Lemon REST API
 */
@RequestScoped
class LemonClient {

    @ConfigProperty(name = "lemon.rest.url")
    lateinit var lemonRestUrl: String

    @ConfigProperty(name = "lemon.rest.database")
    lateinit var lemonRestDatabase: String

    @ConfigProperty(name = "lemon.rest.apikey")
    lateinit var lemonRestApiKey: String

    @ConfigProperty(name = "lemon.rest.username")
    lateinit var lemonRestUsername: String

    @ConfigProperty(name = "lemon.rest.password")
    lateinit var lemonRestPassword: String

    private var sessionId: String? = null

    /**
     * Post construct
     */
    @PostConstruct
    fun init() {
        sessionId = null
    }

    /**
     * Returns single product from Lemonsoft REST API
     *
     * @param productId product id
     * @return product or null if not found
     */
    fun findProduct(productId: Int): Product? {
        return getProductsApi().getProduct(id = productId).result
    }

    /**
     * Lists products from Lemonsoft
     *
     * @param filterName filter by name
     * @param filterSku filter by SKU
     * @param filterWithImages  filter with images
     * @param filterModifiedBefore  filter by modified before
     * @param filterModifiedAfter  filter by modified after
     * @param filterAttributeId  filter by attribute id
     * @param filterExtraName  filter by extra name
     * @param filterCategoryId  filter by category id
     * @param filterGroupCode  filter by group code
     * @param filterShowModels filter by show models
     * @param filterShowNonactive filter by show nonactive
     * @param filterShelf filter by shelf
     * @param filterShelfStock filter by shelf stock
     * @param filterObjectIds filter by object ids
     * @param filterPage Page number. If not provided, using default value of 1 (optional)
     * @param filterPageSize Page size. If not provided, using default value of 10 (optional)
     * @param filterSearch filter by search
     * @return list of products
     */
    fun listProducts(
        filterName: String? = null,
        filterSku: Array<String>? = null,
        filterWithImages: Boolean? = null,
        filterModifiedBefore: String? = null,
        filterModifiedAfter: String? = null,
        filterAttributeId: Int? = null,
        filterExtraName: String? = null,
        filterCategoryId: Int? = null,
        filterGroupCode: Int? = null,
        filterShowModels: Boolean? = null,
        filterShowNonactive: Boolean? = null,
        filterShelf: String? = null,
        filterShelfStock: Int? = null,
        filterObjectIds: Array<Int>? = null,
        filterPage: Int? = null,
        filterPageSize: Int? = null,
        filterSearch: String? = null
    ): Array<Product> {
        return getProductsApi().listProducts(
            filterName = filterName,
            filterSku = filterSku,
            filterWithImages = filterWithImages,
            filterModifiedBefore = filterModifiedBefore,
            filterModifiedAfter = filterModifiedAfter,
            filterAttributeId = filterAttributeId,
            filterExtraName = filterExtraName,
            filterCategoryId = filterCategoryId,
            filterGroupCode = filterGroupCode,
            filterShowModels = filterShowModels,
            filterShowNonactive = filterShowNonactive,
            filterShelf = filterShelf,
            filterShelfStock = filterShelfStock,
            filterObjectIds = filterObjectIds,
            filterPage = filterPage,
            filterPageSize = filterPageSize,
            filterSearch = filterSearch
        ).results
    }

    /**
     * Returns product structure from Lemonsoft REST API
     *
     * @param productCode product code
     * @param workNumber Work number. Use 0 for default structure
     * @param level Structure level to be fetched
     * @return product structure or null if not found
     */
    fun getProductStructure(productCode: String, workNumber: Int, level: Int): GetProductStructureResultResult? {
        return getProductsApi().getProductStructure(
            productCode = productCode,
            workNumber = workNumber,
            level = level
        ).result
    }

    /**
     * Returns initialized product API
     *
     * @return product API
     */
    private fun getProductsApi(): ProductApi {
        ensureSession()

        return ProductApi(
            basePath = lemonRestUrl
        )
    }

    /**
     * Ensures that session is active
     */
    private fun ensureSession() {
        if (sessionId == null) {
            login()
        }

        ApiClient.apiKey["Session-Id"] = sessionId ?: throw LemonException("Session id is null")
    }

    /**
     * Logs in to Lemonsoft REST API
     */
    private fun login() {
        val authenticationApi = AuthenticationApi(
            basePath = lemonRestUrl
        )

        val result = authenticationApi.login(
            LoginPayload(
                database = lemonRestDatabase,
                apiKey = lemonRestApiKey,
                userName = lemonRestUsername,
                password = lemonRestPassword
            )
        )

        sessionId = result.sessionId
    }

}