package fi.metatavu.plastep.productinformation.test.functional.tests

import fi.metatavu.plastep.productinformation.test.functional.resources.LemonRestResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusIntegrationTest
import org.junit.Assert.*

/**
 * Native test for product endpoints
 */
@QuarkusIntegrationTest
@QuarkusTestResource.List(
    QuarkusTestResource(LemonRestResource::class)
)
@Suppress ("UNUSED")
class NativeProductTestIT: ProductTestIT() {


}