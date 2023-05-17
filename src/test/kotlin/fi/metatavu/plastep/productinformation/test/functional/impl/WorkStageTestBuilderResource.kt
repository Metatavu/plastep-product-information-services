package fi.metatavu.plastep.productinformation.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.plastep.productinformation.client.apis.WorkStagesApi
import fi.metatavu.plastep.productinformation.client.infrastructure.ApiClient
import fi.metatavu.plastep.productinformation.client.infrastructure.ClientException
import fi.metatavu.plastep.productinformation.client.models.MainWorkStage
import fi.metatavu.plastep.productinformation.client.models.WorkStagesListResponse
import fi.metatavu.plastep.productinformation.test.functional.TestBuilder
import fi.metatavu.plastep.productinformation.test.functional.settings.ApiTestSettings
import org.junit.jupiter.api.fail

/**
 * Test Builder resource for Work stages api
 */
class WorkStageTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
) : ApiTestBuilderResource<MainWorkStage, ApiClient?>(testBuilder, apiClient) {
    override fun clean(t: MainWorkStage?) {
        // work stages are read-only
    }

    override fun getApi(): WorkStagesApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return WorkStagesApi(ApiTestSettings.apiBasePath)
    }

    /**
     * Lists work stages from API
     *
     * @param updatedBefore updated before
     * @param updatedAfter updated after
     * @param page page number. Page number starts from 1
     * @param pageSize page size. Defaults to 100
     */
    fun list(
        updatedBefore: String,
        updatedAfter: String,
        page: Int?,
        pageSize: Int?
    ): WorkStagesListResponse {
        return api.listWorkStages(
            updatedBefore = updatedBefore,
            updatedAfter = updatedAfter,
            page = page,
            pageSize = pageSize
        )
    }

    /**
     * Asserts that list fails with given status
     *
     * @param expectedStatus expected status
     * @param updatedBefore updated before
     * @param updatedAfter updated after
     * @param page page number
     * @param pageSize page size
     */
    fun assertListFail(
        expectedStatus: Int,
        updatedBefore: String,
        updatedAfter: String,
        page: Int? = null,
        pageSize: Int? = null
    ) {
        try {
            api.listWorkStages(
                updatedBefore = updatedBefore,
                updatedAfter = updatedAfter,
                page = page,
                pageSize = pageSize
            )
            fail(String.format("Expected list to fail with status %d", expectedStatus))
        } catch (ex: ClientException) {
            assertClientExceptionStatus(expectedStatus, ex)
        }
    }

}