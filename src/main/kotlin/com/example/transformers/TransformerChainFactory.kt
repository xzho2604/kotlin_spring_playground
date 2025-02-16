package com.example.transformers

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service


// Base interface for all transformers
interface Transformer<in I, out O> {
    fun transform(input: I): O
}

// Enum to identify different types of responses
enum class ActionType {
    SLACK_MESSAGE,
    discord_send_message,
    DISCORD_MESSAGE,
    EMAIL_NOTIFICATION,
    SMS_NOTIFICATION
    // Add more types as needed
}

class TransformerChain<I, O>(private val transformers: List<Transformer<*, *>>) {
    @Suppress("UNCHECKED_CAST")
    fun execute(input: I): O {
        return transformers.fold(input as Any) { acc, transformer ->
            transformer as Transformer<Any, Any>
            transformer.transform(acc)
        } as O
    }
}

interface TransformerChainFactory {
    val provider: String //TODO: this could be an enum
    val actionType: ActionType

    fun <I, O> createOutputTransformerChainFromConfig(actionType: ActionType): TransformerChain<I, O>
    fun <I, O> createInputTransformerChainFromConfig(actionType: ActionType): TransformerChain<I, O>
}


@Service
class TransformerFactoryService(
    private val applicationContext: ApplicationContext
) {
    private var transformerFactories: List<TransformerChainFactory> = getTransformerFactories()

    fun getTransformerFactories(): List<TransformerChainFactory> {
        return applicationContext.getBeansOfType(TransformerChainFactory::class.java)
            .values
            .toList()
    }

    fun <I, O> getInputTransformerChain(provider: String, actionType: ActionType): TransformerChain<I, O> {
        return transformerFactories
            .find { it.provider == provider && it.actionType == actionType }
            ?.createInputTransformerChainFromConfig(actionType)
            ?: throw IllegalArgumentException("No transformer factory found for provider: $provider and action type: $actionType")
    }

    fun <I, O> getOutputTransformerChain(provider: String, actionType: ActionType): TransformerChain<I, O> {
        return transformerFactories
            .find { it.provider == provider && it.actionType == actionType }
            ?.createOutputTransformerChainFromConfig(actionType)
            ?: throw IllegalArgumentException("No transformer factory found for provider: $provider and action type: $actionType")
    }
}


