package br.com.example.bet.lottery.config

import br.com.example.bet.lottery.domain.BetOrder
import br.com.example.bet.lottery.domain.People
import br.com.example.bet.lottery.domain.commands.LotteryCommandHandler
import br.com.example.bet.lottery.workflow.WorkflowManager
import br.com.example.bet.lottery.workflow.camunda.util.StepDiscoveryService
import br.com.zup.eventsourcing.core.Repository
import br.com.zup.eventsourcing.core.RepositoryManager
import org.apache.logging.log4j.LogManager
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.net.InetAddress

/**
 * Created by marcosgm on 06/12/17
 */
@SpringBootApplication
@EnableProcessApplication
@Configuration
@ComponentScan(basePackages = ["br.com.example.bet.lottery"])
open class ApplicationConfig @Autowired constructor(val betRepository: List<Repository<BetOrder>>,
                                                    val peopleRepository: List<Repository<People>>) {

    @Bean
    @Autowired
    open fun getWorkflow(runtimeService: RuntimeService,
                         commandHandler: LotteryCommandHandler,
                         stepDiscoveryService: StepDiscoveryService) =
            WorkflowManager(runtimeService,
                    commandHandler,
                    stepDiscoveryService)

    @Bean
    open fun betRepositoryManager(): RepositoryManager<BetOrder> =
            RepositoryManager(betRepository)

    @Bean
    open fun peopleRepositoryManager(): RepositoryManager<People> =
            RepositoryManager(peopleRepository)

    @Bean
    @Autowired
    open fun lotteryCommandHandler(betRepositoryManager : RepositoryManager<BetOrder>,
                                   peopleRepositoryManager: RepositoryManager<People>) =
            LotteryCommandHandler(
                    repositoryOrder = betRepositoryManager,
                    repositoryPeople = peopleRepositoryManager
            )


}

private val logger = LogManager.getLogger("br.com.example.bet.lottery.config.ApplicationConfig")


fun main(args: Array<String>) {
    val app = SpringApplication.run(ApplicationConfig::class.java, *args)

    val applicationName = app.environment.getProperty("spring.application.name")
    val contextPath = app.environment.getProperty("server.contextPath")
    val port = app.environment.getProperty("server.port")
    val hostAddress = InetAddress.getLocalHost().hostAddress

    logger.info("""|
                   |------------------------------------------------------------
                   |Application '$applicationName' is running! Access URLs:
                   |   Local:      http://127.0.0.1:$port$contextPath
                   |   External:   http://$hostAddress:$port$contextPath
                   |------------------------------------------------------------""".trimMargin())

}