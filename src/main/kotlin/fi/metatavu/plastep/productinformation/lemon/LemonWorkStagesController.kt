package fi.metatavu.plastep.productinformation.lemon

import fi.metatavu.plastep.lemon.client.models.MainWorkStage
import fi.metatavu.plastep.lemon.client.models.MainWorkStageResponse
import fi.metatavu.plastep.lemon.client.models.WorkStageListResponse
import org.slf4j.Logger
import java.time.LocalDate
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
     * @return workStage response
     */
    fun findWorkStage(workStageId: Long): MainWorkStageResponse {
        return lemonClient.findWorkStage(workStageId = workStageId)
    }

    /**
     * Lists workStages from Lemonsoft
     *
     * @param updatedAfter updated after
     * @param updatedBefore updated before
     * @param page page number. Page number starts from 0
     * @param pageSize page size.
     * @return response and list of workStages with filled info
     */
    fun listWorkStages(
        updatedAfter: LocalDate,
        updatedBefore: LocalDate,
        page: Int,
        pageSize: Int
    ): Pair<WorkStageListResponse, List<MainWorkStage>> {
        val mainWorkStageResponse = lemonClient.listWorkStages(
            filterUpdatedAfter = updatedAfter,
            filterUpdatedBefore = updatedBefore,
            filterPage = page,
            filterPageSize = pageSize
        )

        val mainWorkStageIds = mainWorkStageResponse.results.map { it.id }

        val withFilledData = mainWorkStageIds.map {
            val foundWorkStage = findWorkStage(it)
            if (foundWorkStage.hasErrors || !foundWorkStage.ok) {
                logger.error("Failed to find workStage with id {}", it)
            }

            foundWorkStage.result
        }
        return mainWorkStageResponse to withFilledData
    }

}