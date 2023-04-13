package fi.metatavu.plastep.productinformation.lemon

import fi.metatavu.plastep.lemon.client.models.MainWorkStage
import org.slf4j.Logger
import java.time.OffsetDateTime
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for Lemonsoft workStages
 */
@ApplicationScoped
class LemonWorkStagesController {

    @Inject
    lateinit var lemonClient: LemonClient

    @Inject
    lateinit var logger: Logger

    /**
     * Find workStage from Lemonsoft
     *
     * @param workStageId workStage id
     * @return workStageId workStage or null if not found
     */
    fun findWorkStage(workStageId: Long): MainWorkStage? {
        return lemonClient.findWorkStage(workStageId = workStageId)
    }

    /**
     * Lists workStages from Lemonsoft
     *
     * @param updatedAfter updated after
     * @param updatedBefore updated before
     * @param page page number. Page number starts from 0
     * @param pageSize page size.
     * @return list of workStages
     */
    fun listWorkStages(
        updatedAfter: OffsetDateTime,
        updatedBefore: OffsetDateTime,
        page: Int,
        pageSize: Int
    ): List<MainWorkStage> {
        val mainWorkStageIds = lemonClient.listWorkStages(
            filterUpdatedAfter = updatedAfter,
            filterUpdatedBefore = updatedBefore,
            filterPage = page,
            filterPageSize = pageSize,
        ).map { it.id }

        return mainWorkStageIds.mapNotNull {
            val foundWorkStage = findWorkStage(it)
            if (foundWorkStage == null) {
                logger.error("Failed to find workStage with id {}", it)
            }

            foundWorkStage
        }
    }

}