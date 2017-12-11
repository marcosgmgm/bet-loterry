package br.com.example.bet.lottery.workflow.camunda.tasks

import br.com.example.bet.lottery.domain.BetOrderId
import br.com.example.bet.lottery.domain.commands.CreateBet
import br.com.example.bet.lottery.domain.commands.LotteryCommandHandler
import br.com.example.bet.lottery.workflow.WorkflowManager
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Service

/**
 * Created by marcosgm on 06/12/17
 */
@Service
open class CreateBetTask (val commandHandler: LotteryCommandHandler): JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        val betOrderId = BetOrderId(execution.getVariable(WorkflowManager.BET_ORDER_ID) as String)
        commandHandler.handle(CreateBet(betOrderId))
    }
}