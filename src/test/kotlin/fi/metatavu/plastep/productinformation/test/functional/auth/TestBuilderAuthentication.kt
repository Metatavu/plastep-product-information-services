package fi.metatavu.plastep.productinformation.test.functional.auth

import fi.metatavu.plastep.productinformation.client.infrastructure.ApiClient
import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenTestBuilderAuthentication
import fi.metatavu.plastep.productinformation.test.functional.TestBuilder
import fi.metatavu.plastep.productinformation.test.functional.impl.MachineTestBuilderResource
import fi.metatavu.plastep.productinformation.test.functional.impl.ProductSubWorkStageTestBuilderResource
import fi.metatavu.plastep.productinformation.test.functional.settings.ApiTestSettings
import fi.metatavu.plastep.productinformation.test.functional.impl.ProductTestBuilderResource
import fi.metatavu.plastep.productinformation.test.functional.impl.WorkStageTestBuilderResource

/**
 * Test builder authentication
 *
 * @author Antti Leppä
 *
 * @param testBuilder test builder instance
 * @param accessTokenProvider access token provider
 */
class TestBuilderAuthentication(
    private val testBuilder: TestBuilder,
    accessTokenProvider: AccessTokenProvider
): AccessTokenTestBuilderAuthentication<ApiClient>(testBuilder, accessTokenProvider) {

    private var accessTokenProvider: AccessTokenProvider? = accessTokenProvider

    val product = ProductTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    val machine = MachineTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    val workStages = WorkStageTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    val productSubWorkStages = ProductSubWorkStageTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())

    override fun createClient(authProvider: AccessTokenProvider): ApiClient {
        val result = ApiClient(ApiTestSettings.apiBasePath)
        ApiClient.accessToken = authProvider.accessToken
        return result
    }

}