package br.com.example.bet.lottery.domain.workflow

import br.com.example.bet.lottery.domain.BetOrder
import br.com.example.bet.lottery.domain.commands.CreateBetOrder

interface Workflow {
    fun start(createBetOrder: CreateBetOrder): BetOrder
}