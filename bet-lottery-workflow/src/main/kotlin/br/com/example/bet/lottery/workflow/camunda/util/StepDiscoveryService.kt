package br.com.example.bet.lottery.workflow.camunda.util

import br.com.example.bet.lottery.domain.BetOrderStep
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.model.bpmn.instance.Activity
import org.camunda.bpm.model.bpmn.instance.BaseElement
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties
import org.springframework.stereotype.Service


@Service
open class  StepDiscoveryService(val repositoryService: RepositoryService) {
    companion object {
        const val NAME = "name"
        const val STEP = "STEP"
    }

    fun getSteps(processDefinitionKey: String): LinkedHashMap<BetOrderStep.ID, BetOrderStep> {

        val steps = linkedMapOf<BetOrderStep.ID, BetOrderStep>()

        val processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult()
        processDefinition?.let { p ->
            val processDefinitionId = p.id
            val modelInstance = repositoryService.getBpmnModelInstance(processDefinitionId)

            val type = modelInstance.model.getType(BaseElement::class.java)
            val instances = modelInstance.getModelElementsByType(type)
            instances.forEach {
                if (it is BaseElement) {
                    if (null != it.extensionElements && it.extensionElements.elements.isNotEmpty()) {
                        if (it.extensionElements.elementsQuery.filterByType(CamundaProperties::class.java).count() > 0) {
                            val camundaProperties = it.extensionElements.elementsQuery.filterByType(CamundaProperties::class.java).singleResult()
                            val properties = camundaProperties.camundaProperties
                            properties.forEach {
                                if (it.getAttributeValue(NAME).toUpperCase() == STEP) {
                                    steps.put(BetOrderStep.ID(it.camundaValue.toUpperCase()), BetOrderStep(BetOrderStep.ID(it.camundaValue.toUpperCase())))
                                }
                            }
                        }

                    }
                }
            }
        }

        return steps
    }

    fun getStep(execution: DelegateExecution): BetOrderStep? {
        val task = execution.bpmnModelElementInstance
        if (task is Activity) {
            val extensionElements = task.extensionElements
            val properties = extensionElements.elementsQuery
                    .filterByType(CamundaProperties::class.java)
                    .singleResult()
                    .camundaProperties
            val property = properties.firstOrNull { it.getAttributeValue(NAME).toUpperCase() == STEP }

            if (null == property) return null else return BetOrderStep(BetOrderStep.ID(property.camundaValue.toUpperCase()))
        }
        return null
    }
}