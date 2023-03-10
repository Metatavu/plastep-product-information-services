package fi.metatavu.plastep.productinformation.lemon

import fi.metatavu.plastep.lemon.client.models.GetProductStructureResultResult
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
     * Find product from Lemonsoft
     *
     * @param productId product id
     * @return productId product or null if not found
     */
    fun findProduct(
        productId: Int
    ): Product? {
        return lemonClient.findProduct(
            productId = productId
        )
    }

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

    /**
     * Returns product's product structure from Lemonsoft REST API. Result contains only single level
     *
     * @param productCode product code
     * @return product structure or null if not found
     */
    fun getDefaultProductStructure(productCode: String): GetProductStructureResultResult? {
        return lemonClient.getProductStructure(
            productCode = productCode,
            workNumber = 0,
            level = 1
        )
    }

}