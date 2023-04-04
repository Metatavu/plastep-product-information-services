package fi.metatavu.plastep.productinformation.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.plastep.productinformation.client.apis.MachinesApi
import fi.metatavu.plastep.productinformation.client.infrastructure.ApiClient
import fi.metatavu.plastep.productinformation.client.infrastructure.ClientException
import fi.metatavu.plastep.productinformation.model.Machine
import fi.metatavu.plastep.productinformation.test.functional.TestBuilder
import fi.metatavu.plastep.productinformation.test.functional.settings.ApiTestSettings
import org.junit.jupiter.api.fail

/**
 * Test Builder resource for Machine api
 */
class MachineTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
) : ApiTestBuilderResource<Machine, ApiClient?>(testBuilder, apiClient) {
    override fun clean(t: Machine?) {
        // machines are read-only
    }

    override fun getApi(): MachinesApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return MachinesApi(ApiTestSettings.apiBasePath)
    }


    /**
     * Lists machines from API
     *
     * @param page page number. Page number starts from 0
     * @param pageSize page size. Defaults to 10
     */
    fun list(page: Int?, pageSize: Int?): Array<fi.metatavu.plastep.productinformation.client.models.Machine> {
        return api.listMachines(
            page = page,
            pageSize = pageSize
        )
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
            api.listMachines(page = page, pageSize = pageSize)
            fail(String.format("Expected list to fail with status %d", expectedStatus))
        } catch (ex: ClientException) {
            assertClientExceptionStatus(expectedStatus, ex)
        }
    }

}