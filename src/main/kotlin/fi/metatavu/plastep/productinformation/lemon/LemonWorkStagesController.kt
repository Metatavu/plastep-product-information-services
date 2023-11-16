package fi.metatavu.plastep.productinformation.lemon

import fi.metatavu.plastep.lemon.client.models.MainWorkStage
import fi.metatavu.plastep.lemon.client.models.MainWorkStageResponse
import fi.metatavu.plastep.lemon.client.models.WorkStageListResponse
import fi.metatavu.plastep.productinformation.model.WorkStageState
import fi.metatavu.plastep.productinformation.workstages.LemonWorkStagesTranslator
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

    @Inject
    lateinit var lemonWorkStagesTranslator: LemonWorkStagesTranslator

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

        val mainWorkStageIds = mainWorkStageResponse.results?.map { it.id } ?: emptyList()

        val withFilledData = mainWorkStageIds.map {
            val foundWorkStage = findWorkStage(it)
            if (foundWorkStage.hasErrors || !foundWorkStage.ok) {
                logger.error("Failed to find workStage with id {}", it)
            }

            foundWorkStage.result
        }
        return mainWorkStageResponse to withFilledData.mapNotNull { it }
    }

    /**
     * Lists available work stages from lemonsoft
     *
     * @param state state filter
     * @param machineCode machine code filter
     * @return list of work stages
     */
    fun listSubWorkStages(
        state: WorkStageState?,
        machineCode: String?
    ): List<fi.metatavu.plastep.lemon.client.models.SubWorkStage> {
        val subWorkStages = lemonClient.listSubWorkStages(
            machineCode = machineCode,
        )

        val filteredByState: List<fi.metatavu.plastep.lemon.client.models.SubWorkStage?>? = if (state == null) {
            subWorkStages.results?.toList()
        } else {
            val stateInt = lemonWorkStagesTranslator.translateWorkStageState(state)
            subWorkStages.results?.filter { it.state == stateInt }
        }

        return filteredByState?.filterNotNull() ?: emptyList()
    }

}