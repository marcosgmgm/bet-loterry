package br.com.example.bet.lottery.workflow.camunda.listener


import br.com.example.bet.lottery.domain.BetOrderId
import br.com.example.bet.lottery.domain.BetOrderStep
import br.com.example.bet.lottery.domain.commands.LotteryCommandHandler
import br.com.example.bet.lottery.domain.commands.UpdateStep
import br.com.example.bet.lottery.workflow.WorkflowManager
import br.com.example.bet.lottery.workflow.camunda.util.StepDiscoveryService
import org.apache.logging.log4j.LogManager
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.camunda.bpm.engine.impl.el.FixedValue
import org.springframework.stereotype.Service

@Service
open class UpdateStepListener(
        val stepDiscoveryService: StepDiscoveryService,
        val commandHandler: LotteryCommandHandler

) : JavaDelegate {

    private val LOG = LogManager.getLogger(this.javaClass)

    lateinit var status: FixedValue

    override fun execute(execution: DelegateExecution) {
        LOG.info("UpdateStep")
        val betOrderId = BetOrderId(execution.getVariable(WorkflowManager.BET_ORDER_ID) as String)
        val step = stepDiscoveryService.getStep(execution)
        if (null != step) updateStepTo(step, BetOrderStep.Status.valueOf(status.expressionText), betOrderId)
    }

    private fun updateStepTo(step: BetOrderStep, status: BetOrderStep.Status, betOrderId: BetOrderId) {
        commandHandler.handle(
                UpdateStep(
                        betOrderId = betOrderId,
                        step = BetOrderStep(
                                id = step.id,
                                status = status
                        )
                )
        )
    }
}