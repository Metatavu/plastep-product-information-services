package fi.metatavu.plastep.productinformation.products

import fi.metatavu.plastep.productinformation.model.Product
import fi.metatavu.plastep.productinformation.rest.AbstractTranslator
import javax.enterprise.context.ApplicationScoped

/**
 * Translates Lemon product to REST product
 *
 * @author Antti Leppä
 */
@ApplicationScoped
class LemonProductTranslator : AbstractTranslator<fi.metatavu.plastep.lemon.client.models.Product, Product>() {

    override fun translate(entity: fi.metatavu.plastep.lemon.client.models.Product): Product {
        return Product(
            originId = entity.id.toString(),
            name = entity.name,
            productCode = entity.sku
        )
    }

}
