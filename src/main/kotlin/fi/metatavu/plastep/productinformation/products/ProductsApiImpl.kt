package fi.metatavu.plastep.productinformation.products

import fi.metatavu.plastep.api.abstracts.AbstractApi
import fi.metatavu.plastep.productinformation.lemon.LemonProductsController
import fi.metatavu.plastep.productinformation.spec.ProductsApi
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.core.Response

@RequestScoped
@Suppress ("UNUSED")
@Consumes ("application/json")
class ProductsApiImpl: ProductsApi, AbstractApi() {

    @Inject
    lateinit var lemonProductsController: LemonProductsController

    @Inject
    lateinit var lemonProductTranslator: LemonProductTranslator

    override fun listProducts(page: Int?, pageSize: Int?): Response {
        val lemonProducts = lemonProductsController.listProducts(
            page ?: 0,
            pageSize ?: 10
        )

        return createOk(lemonProducts.map { lemonProductTranslator.translate(it) })
    }

}