package fi.metatavu.plastep.productinformation.test.functional

import fi.metatavu.jaxrs.test.functional.builder.AbstractAccessTokenTestBuilder
import fi.metatavu.jaxrs.test.functional.builder.AbstractTestBuilder
import fi.metatavu.jaxrs.test.functional.builder.auth.*
import fi.metatavu.plastep.productinformation.client.infrastructure.ApiClient
import fi.metatavu.plastep.productinformation.test.functional.auth.TestBuilderAuthentication
import java.net.URL

/**
 * Abstract test builder class
 *
 * @param config test config
 * @author Antti Leppä
 */
class TestBuilder(private val config: Map<String, String>): AbstractAccessTokenTestBuilder<ApiClient>() {

    val admin: TestBuilderAuthentication = createAuthentication("admin")
    val integration: TestBuilderAuthentication = createAuthentication("integration")
    val empty: TestBuilderAuthentication = TestBuilderAuthentication(this, NullAccessTokenProvider())
    val notvalid: TestBuilderAuthentication = TestBuilderAuthentication(this, InvalidAccessTokenProvider())

    override fun createTestBuilderAuthentication(
            abstractTestBuilder: AbstractTestBuilder<ApiClient, AccessTokenProvider>,
            authProvider: AccessTokenProvider
    ): AuthorizedTestBuilderAuthentication<ApiClient, AccessTokenProvider> {
        return TestBuilderAuthentication(this, authProvider)
    }

    /**
     * Returns test resource authentication as given user
     *
     * @param username username of a test user
     * @return authenticated test resource
     */
    private fun createAuthentication(username: String): TestBuilderAuthentication {
        val authServerUrl = config.getValue("quarkus.oidc.auth-server-url").substringBeforeLast("/").substringBeforeLast("/")
        val realm = getKeycloakRealm()
        val clientId = "ui"
        val password = "test"
        return TestBuilderAuthentication(this, KeycloakAccessTokenProvider(authServerUrl, realm, clientId, username, password, null))
    }

    /**
     * Resolves used Keycloak realm from auth server URL
     *
     * @return resolved Keycloak realm
     */
    private fun getKeycloakRealm(): String {
        val serverUrl = URL(config["quarkus.oidc.auth-server-url"]!!)
        val pattern = Regex("(/realms/)([a-z]*)")
        val match = pattern.find(serverUrl.path)!!
        val (_, realm) = match.destructured
        return realm
    }

}