package fi.metatavu.plastep.productinformation.lemon

import fi.metatavu.plastep.lemon.client.models.Machine
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for Lemonsoft machines
 */
@ApplicationScoped
class LemonMachinesController {

    @Inject
    lateinit var lemonClient: LemonClient

    /**
     * List machines from Lemonsoft
     *
     * @param page page number.
     * @param pageSize page size.
     * @return list of machines
     */
    fun listMachines(
        page: Int,
        pageSize: Int
    ): Array<Machine> {
        val machines = lemonClient.listMachines(
            filterPage = page + 1,
            filterPageSize = pageSize,
        )
        return machines
    }

}