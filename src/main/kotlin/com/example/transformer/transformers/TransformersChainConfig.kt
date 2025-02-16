package com.example.transformer.transformers

import com.example.transformer.transformers.ActionType
import com.example.transformer.transformers.Transformer
import com.example.transformer.transformers.TransformerChain
import com.example.transformer.transformers.TransformerChainFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

@ConfigurationProperties
data class TransformerConfig(
    val outputTransformers: Map<String, List<String>>,
    val inputTransformers: Map<String, List<String>>
)
@Configuration
abstract class BaseTransformerConfiguration(
    private val configFile: Resource
) {
    @Bean
    fun transformerConfig(): TransformerConfig {
        val mapper = ObjectMapper(YAMLFactory()).apply {
            registerModule(KotlinModule.Builder().build())
        }

        return mapper.readValue(configFile.inputStream, TransformerConfig::class.java)
    }
}

abstract class BaseTransformerChainConfig(
    private val transformerConfig: TransformerConfig,
    private val applicationContext: ApplicationContext,
): TransformerChainFactory {
    abstract override val provider: String
    abstract override val actionType: ActionType

    override fun <I, O> createOutputTransformerChainFromConfig(actionType: ActionType): TransformerChain<I, O> {
        val transformers = transformerConfig.outputTransformers[actionType.name]?.map { name ->
            applicationContext.getBean(name, Transformer::class.java)
        }

        @Suppress("UNCHECKED_CAST")
        return TransformerChain<I, O>(transformers as List<Transformer<Any, Any>>)
    }

    override fun <I, O> createInputTransformerChainFromConfig(actionType: ActionType): TransformerChain<I, O> {
        val transformers = transformerConfig.inputTransformers[actionType.name]?.map { name ->
            applicationContext.getBean(name, Transformer::class.java)
        }

        @Suppress("UNCHECKED_CAST")
        return TransformerChain<I, O>(transformers as List<Transformer<Any, Any>>)
    }
}

