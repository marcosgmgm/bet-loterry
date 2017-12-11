package br.com.example.bet.lottery.workflow

import br.com.example.bet.lottery.domain.BetOrder
import br.com.example.bet.lottery.domain.commands.CreateBetOrder
import br.com.example.bet.lottery.domain.commands.LotteryCommandHandler
import br.com.example.bet.lottery.domain.workflow.Workflow
import br.com.example.bet.lottery.workflow.camunda.util.StepDiscoveryService
import org.camunda.bpm.engine.RuntimeService

/**
 * Created by marcosgm on 06/12/17
 */
class WorkflowManager(private val runtimeService: RuntimeService,
                      private val commandHandler: LotteryCommandHandler,
                      private val stepDiscoveryService: StepDiscoveryService): Workflow {

    companion object {
        const val BET_ORDER_ID = "betOrderId"
        const val PROCESS_NAME = "BET_PROCESS"
    }

    override fun start(createBetOrder: CreateBetOrder): BetOrder {
        val steps = stepDiscoveryService.getSteps(PROCESS_NAME)
        val betOrder = commandHandler.handle(createBetOrder.copy(steps = steps))
        val variables = mutableMapOf<String, Any>()
        variables.put(BET_ORDER_ID, betOrder.idAsString())
        runtimeService.startProcessInstanceByKey(PROCESS_NAME, betOrder.idAsString(), variables)
        return betOrder
    }




}