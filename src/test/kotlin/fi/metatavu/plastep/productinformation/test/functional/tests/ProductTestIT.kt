package fi.metatavu.plastep.productinformation.test.functional.tests

import fi.metatavu.plastep.productinformation.client.models.Product
import fi.metatavu.plastep.productinformation.test.functional.resources.LemonRestResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.Assert.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Tests for product endpoints
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(LemonRestResource::class)
)
class ProductTestIT: AbstractResourceTest() {

    @Test
    fun testFindProduct() = createTestBuilder().use {
        val product = it.integration.product.find(id = 12345)
        Assertions.assertEquals(12345, product.id)
        Assertions.assertEquals("000123", product.productCode)
        Assertions.assertEquals("Test product 1", product.name)
    }

    @Test
    fun testFindProductUnauthorized() = createTestBuilder().use {
        it.admin.product.assertFindFail(expectedStatus = 403, id = 12345)
        it.empty.product.assertFindFail(expectedStatus = 401, id = 12345)
        it.notvalid.product.assertFindFail(expectedStatus = 401, id = 12345)
    }

    @Test
    fun testListProducts() = createTestBuilder().use {
        val allProducts = it.integration.product.list(page = null, pageSize = null)
        Assertions.assertEquals(4, allProducts.size)

        Assertions.assertEquals(12345, allProducts[0].id)
        Assertions.assertEquals("000123", allProducts[0].productCode)

        Assertions.assertArrayEquals(arrayOf("Test product 1", "Test product 2", "Test product 3", "Test product 4"), allProducts.map(Product::name).toTypedArray())

        val filteredProducts1 = it.integration.product.list(page = 1, pageSize = 2)
        Assertions.assertEquals(2, filteredProducts1.size)
        Assertions.assertArrayEquals(arrayOf("Test product 2", "Test product 3"), filteredProducts1.map(Product::name).toTypedArray())

        val filteredProducts2 = it.integration.product.list(page = 2, pageSize = 2)
        Assertions.assertEquals(2, filteredProducts2.size)
        Assertions.assertArrayEquals(arrayOf("Test product 3", "Test product 4"), filteredProducts2.map(Product::name).toTypedArray())

        val filteredProducts3 = it.integration.product.list(page = 0, pageSize = 0)
        Assertions.assertEquals(0, filteredProducts3.size)
    }

    @Test
    fun testListProductsInvalidUser() = createTestBuilder().use {
        it.admin.product.assertListFail(expectedStatus = 403)
        it.empty.product.assertListFail(expectedStatus = 401)
        it.notvalid.product.assertListFail(expectedStatus = 401)
    }

}