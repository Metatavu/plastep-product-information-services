package fi.metatavu.plastep.productinformation.workstages

import fi.metatavu.plastep.productinformation.lemon.LemonWorkStagesController
import fi.metatavu.plastep.productinformation.model.WorkStageState
import fi.metatavu.plastep.productinformation.rest.AbstractApi
import fi.metatavu.plastep.productinformation.spec.WorkStagesApi
import java.time.OffsetDateTime
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.core.Response

@RequestScoped
@Suppress ("UNUSED")
@Consumes ("application/json")
class WorkStagesApiImpl: WorkStagesApi, AbstractApi() {

    @Inject
    lateinit var lemonWorkStagesController: LemonWorkStagesController

    @Inject
    lateinit var lemonWorkStagesTranslator: LemonWorkStagesTranslator

    override fun findWorkStage(workStageId: Long): Response {
        val workStage = lemonWorkStagesController.findWorkStage(workStageId) ?: return createNotFoundWithMessage(WORK_STAGE, workStageId.toInt())
        return createOk(lemonWorkStagesTranslator.translate(workStage))
    }

    override fun listWorkStages(
        updatedAfter: OffsetDateTime,
        updatedBefore: OffsetDateTime,
        page: Int?,
        pageSize: Int?,
        state: WorkStageState?
    ): Response {
        val workStages = lemonWorkStagesController.listWorkStages(
            updatedAfter = updatedAfter,
            updatedBefore = updatedBefore,
            page = page ?: 1,
            pageSize = pageSize ?: 100,
        )

        return createOk(workStages.map(lemonWorkStagesTranslator::translate))
    }


}