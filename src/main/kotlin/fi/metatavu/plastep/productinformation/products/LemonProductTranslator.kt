package fi.metatavu.plastep.productinformation.products

import fi.metatavu.plastep.lemon.client.models.ProductStructureNode
import fi.metatavu.plastep.productinformation.lemon.LemonProductsController
import fi.metatavu.plastep.productinformation.model.Product
import fi.metatavu.plastep.productinformation.rest.AbstractTranslator
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Translates Lemon product to REST product
 *
 * @author Antti Leppä
 */
@ApplicationScoped
class LemonProductTranslator : AbstractTranslator<fi.metatavu.plastep.lemon.client.models.Product, Product>() {

    @Inject
    lateinit var lemonProductsController: LemonProductsController

    override fun translate(entity: fi.metatavu.plastep.lemon.client.models.Product): Product {
        val structure = lemonProductsController.getDefaultProductStructure(productCode = entity.sku)
        val childProducts = structure?.nodes?.mapNotNull(ProductStructureNode::node) ?: emptyList()

        return Product(
            id = entity.id,
            name = entity.name,
            productCode = entity.sku,
            childProductCodes = childProducts.mapNotNull { it.productCode },
            childProductIds = childProducts.mapNotNull { it.productId }
        )
    }

}
