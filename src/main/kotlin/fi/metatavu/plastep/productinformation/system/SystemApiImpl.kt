package fi.metatavu.plastep.productinformation.system

import fi.metatavu.plastep.productinformation.rest.AbstractApi
import fi.metatavu.plastep.productinformation.spec.SystemApi
import javax.ws.rs.Path
import javax.ws.rs.core.Response

/**
 * Class for system API implementation
 *
 * @author Antti Leppä
 */
@Path("/")
class SystemApiImpl: SystemApi, AbstractApi() {

    override fun ping(): Response {
        return createOk("pong")
    }

}