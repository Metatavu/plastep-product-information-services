package fi.metatavu.plastep.productinformation.lemon

import fi.metatavu.plastep.lemon.client.models.Product
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for Lemonsoft products
 */
@ApplicationScoped
class LemonProductsController {

    @Inject
    lateinit var lemonClient: LemonClient

    /**
     * Lists products from Lemonsoft
     *
     * @param page page number. Page number starts from 0
     * @param pageSize page size.
     * @return list of products
     */
    fun listProducts(
        page: Int,
        pageSize: Int
    ): Array<Product> {
        return lemonClient.listProducts(
            filterPage = page + 1,
            filterPageSize = pageSize,
        )
    }

}