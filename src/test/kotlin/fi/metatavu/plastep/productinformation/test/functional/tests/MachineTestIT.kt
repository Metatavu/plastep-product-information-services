package fi.metatavu.plastep.productinformation.test.functional.tests

import fi.metatavu.plastep.productinformation.test.functional.resources.LemonRestResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

/**
 * Tests for machines endpoints
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(LemonRestResource::class)
)
class MachineTestIT : AbstractResourceTest() {

    @Test
    fun list() {
        createTestBuilder().use { it ->
            val machines = it.integration.machine.list(page = null, pageSize = null)
            assertEquals(4, machines.size)

            assertEquals(1, machines[0].id)
            assertEquals("machine code 1", machines[0].code)
            assertEquals("machine description", machines[0].description)

            val machinesPage1 = it.integration.machine.list(page = 1, pageSize = 2)
            assertEquals(2, machinesPage1.size)
            assertEquals(2, machinesPage1.sortedBy { it.id }[0].id)

            val machinesPage2 = it.integration.machine.list(page = 2, pageSize = 2)
            assertEquals(2, machinesPage2.size)
            assertEquals(3, machinesPage2.sortedBy { it.id }[0].id)

            val machinesEmpty = it.integration.machine.list(page = 2, pageSize = 0)
            assertEquals(0, machinesEmpty.size)

            // access rights
            it.admin.product.assertListFail(expectedStatus = 403)
            it.empty.product.assertListFail(expectedStatus = 401)
            it.notvalid.product.assertListFail(expectedStatus = 401)
        }
    }

}