package fi.metatavu.plastep.productinformation.machines

import fi.metatavu.plastep.productinformation.lemon.LemonMachinesController
import fi.metatavu.plastep.productinformation.rest.AbstractApi
import fi.metatavu.plastep.productinformation.spec.MachinesApi
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@RequestScoped
class MachinesApiImpl : MachinesApi, AbstractApi() {

    @Inject
    lateinit var lemonMachinesController: LemonMachinesController

    @Inject
    lateinit var lemonMachineTranslator: LemonMachineTranslator

    override fun listMachines(page: Int?, pageSize: Int?): Response {
        if (!hasIntegrationRole()) {
            return createForbidden("Only integration users can access this resource")
        }

        val lemonMachines = lemonMachinesController.listMachines(
            page ?: 0,
            pageSize ?: 50
        )

        return createOk(lemonMachines.map { lemonMachineTranslator.translate(it) })
    }
}