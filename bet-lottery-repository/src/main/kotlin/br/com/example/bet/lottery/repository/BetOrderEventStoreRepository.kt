package br.com.example.bet.lottery.repository

import br.com.example.bet.lottery.domain.BetOrder
import br.com.zup.eventsourcing.eventstore.EventStoreRepository
import org.springframework.stereotype.Service
import javax.annotation.Priority


/**
 * Created by marcosgm on 05/12/17
 */
@Service
@Priority(1)
class BetOrderEventStoreRepository : EventStoreRepository<BetOrder>()