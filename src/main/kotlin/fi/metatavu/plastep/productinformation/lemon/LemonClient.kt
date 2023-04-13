package fi.metatavu.plastep.productinformation.lemon

import fi.metatavu.plastep.lemon.client.apis.AuthenticationApi
import fi.metatavu.plastep.lemon.client.apis.MachineApi
import fi.metatavu.plastep.lemon.client.apis.ProductApi
import fi.metatavu.plastep.lemon.client.apis.WorkStagesApi
import fi.metatavu.plastep.lemon.client.infrastructure.ApiClient
import fi.metatavu.plastep.lemon.client.models.*
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.Logger
import java.time.OffsetDateTime
import javax.annotation.PostConstruct
import javax.enterprise.context.RequestScoped
import javax.inject.Inject

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

    @Inject
    lateinit var logger: Logger

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
     * Returns machines from Lemonsoft REST API
     *
     * @param filterCode filter by code
     * @param filterType filter by type
     * @param filterIsDisabled filter by is disabled
     * @param filterObjectIds filter by object ids
     * @param filterPage Page number.
     * @param filterPageSize Page size.
     *  @param filterSearch filter by search
     */
    fun listMachines(
        filterCode: String? = null,
        filterType: Int? = null,
        filterIsDisabled: Boolean? = null,
        filterObjectIds: Array<Int>? = null,
        filterPage: Int? = null,
        filterPageSize: Int? = null,
        filterSearch: String? = null
    ): Array<Machine> {
        return getMachinesApi().listMachines(
            filterCode = filterCode,
            filterType = filterType,
            filterIsDisabled = filterIsDisabled,
            filterObjectIds = filterObjectIds,
            filterPage = filterPage,
            filterPageSize = filterPageSize,
            filterSearch = filterSearch
        ).results
    }

    /**
     * Returns single main work stage from Lemonsoft REST API
     *
     * @param workStageId work stage id
     * @return Work stage or null if not found
     */
    fun findWorkStage(workStageId: Long): MainWorkStage? {
        val response = getWorkStagesApi().findWorkStage(id = workStageId)

        return if (response.ok) {
            response.result
        } else {
            null
        }
    }

    /**
     * Lists work stages from Lemonsoft
     *
     * @param filterUpdatedAfter filter by updated after
     * @param filterUpdatedBefore filter by updated before
     * @param filterPage Page number. If not provided, using default value of 1 (optional)
     * @param filterPageSize Page size. If not provided, using default value of 10 (optional)
     * @param filterState filter by state
     * @return list of work stages
     */
    fun listWorkStages(
        filterUpdatedAfter: OffsetDateTime,
        filterUpdatedBefore: OffsetDateTime,
        filterPage: Int,
        filterPageSize: Int,
        filterState: Int? = null
    ): Array<WorkStageListItem> {
        val response = getWorkStagesApi().listWorkStages(
            filterUpdatedAfter = filterUpdatedAfter.toString(),
            filterUpdatedBefore = filterUpdatedBefore.toString(),
            filterPage = filterPage,
            filterPageSize = filterPageSize,
            filterState = filterState
        )

        return if (response.ok) {
            response.results
        } else {
            logger.error("Failed to list work stages")
            emptyArray()
        }
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
     * Returns initialized machine API
     *
     * @return machine API
     */
    private fun getMachinesApi(): MachineApi {
        ensureSession()

        return MachineApi(
            basePath = lemonRestUrl
        )
    }

    /**
     * Returns initialized work stage API
     *
     * @return work stage API
     */
    private fun getWorkStagesApi(): WorkStagesApi {
        ensureSession()

        return WorkStagesApi(
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