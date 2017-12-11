package br.com.example.bet.lottery.domain

import br.com.zup.eventsourcing.core.AggregateId
import com.fasterxml.jackson.annotation.JsonCreator

/**
 * Created by marcosgm on 04/12/17
 */
class CPF @JsonCreator constructor(value: String) : AggregateId(value)

class Name(val name: String)

class Balance(val value: Long)