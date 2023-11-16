package fi.metatavu.plastep.productinformation.test.functional.tests

import fi.metatavu.plastep.productinformation.test.functional.resources.LemonRestResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Tests for Product Sub Work stages API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(LemonRestResource::class)
)
class ProductSubWorkStageTestIT: AbstractResourceTest() {

    /**
     * Tests that it is possible to request sub work stages that are happening in the machine
     */
    @Test
    fun getMachineSubWorkStages() {
        createTestBuilder().use { tb ->
            val productSubWorkStages = tb.admin.productSubWorkStages.list(
                state = null
            )

            assertEquals(1, productSubWorkStages.size)
            assertEquals(1, productSubWorkStages[0].id)
            assertEquals(37, productSubWorkStages[0].machineId)
            assertEquals("RVH", productSubWorkStages[0].machineCode)
            assertEquals("000223", productSubWorkStages[0].productCode)
            assertEquals(fi.metatavu.plastep.productinformation.client.models.WorkStageState.ACCEPTED, productSubWorkStages[0].state)
            assertEquals("2021-01-22T14:00:00Z", productSubWorkStages[0].startTime)
            assertEquals(null, productSubWorkStages[0].endTime)
            assertEquals("Test product 2", productSubWorkStages[0].productName)
            assertEquals(5400.0.toFloat(), productSubWorkStages[0].workAmount)
            assertEquals(0.0.toFloat(), productSubWorkStages[0].workAmountDone)


            tb.integration.productSubWorkStages.assertListFailStatus(
                403,
                null,
                null
            )

            tb.integration.productSubWorkStages.assertListFailStatus(
                403,
                null,
                null
            )

        }
    }
}