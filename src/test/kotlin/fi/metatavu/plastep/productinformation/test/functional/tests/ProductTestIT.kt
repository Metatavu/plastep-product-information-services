package fi.metatavu.plastep.productinformation.test.functional.tests

import fi.metatavu.plastep.productinformation.client.models.Product
import fi.metatavu.plastep.productinformation.test.functional.resources.LemonRestResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

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
        assertEquals(false, product.hasErrors)
        assertEquals(true, product.ok)
        assertEquals(12345, product.product!!.id)
        assertEquals("000123", product.product.productCode)
        assertEquals("Test product 1", product.product.name)
        assertArrayEquals(arrayOf("000223", "000323"), product.product.childProductCodes)
        assertArrayEquals(arrayOf(12346L, 12347L), product.product.childProductIds)
    }

    @Test
    fun testFindProductUnauthorized() = createTestBuilder().use {
        it.admin.product.assertFindFail(expectedStatus = 403, id = 12345)
        it.empty.product.assertFindFail(expectedStatus = 401, id = 12345)
        it.notvalid.product.assertFindFail(expectedStatus = 401, id = 12345)
    }

    @Test
    fun testListProducts() = createTestBuilder().use {
        val allProducts = it.integration.product.list(page = null, pageSize = null).products!!
        assertEquals(4, allProducts.size)

        assertEquals(12345, allProducts[0].id)
        assertEquals("000123", allProducts[0].productCode)
        assertArrayEquals(arrayOf("000223", "000323"), allProducts[0].childProductCodes)
        assertArrayEquals(arrayOf(12346L, 12347L), allProducts[0].childProductIds)

        assertArrayEquals(arrayOf("Test product 1", "Test product 2", "Test product 3", "Test product 4"), allProducts.map(Product::name).toTypedArray())

        val filteredProducts1 = it.integration.product.list(page = 1, pageSize = 2)
        assertEquals(true, filteredProducts1.hasNextPage)
        assertEquals(false, filteredProducts1.hasErrors)
        assertEquals(2, filteredProducts1.products!!.size)
        assertArrayEquals(arrayOf("Test product 2", "Test product 3"), filteredProducts1.products.map(Product::name).toTypedArray())

        val filteredProducts2 = it.integration.product.list(page = 2, pageSize = 2).products!!
        assertEquals(2, filteredProducts2.size)
        assertArrayEquals(arrayOf("Test product 3", "Test product 4"), filteredProducts2.map(Product::name).toTypedArray())
    }

    @Test
    fun testListProductsInvalidUser() = createTestBuilder().use {
        it.admin.product.assertListFail(expectedStatus = 403)
        it.empty.product.assertListFail(expectedStatus = 401)
        it.notvalid.product.assertListFail(expectedStatus = 401)
    }

}