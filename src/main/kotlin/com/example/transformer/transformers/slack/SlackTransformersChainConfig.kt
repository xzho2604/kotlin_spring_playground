package com.example.transformer.transformers.slack

import com.example.transformer.transformers.ActionType
import com.example.transformer.transformers.Transformer
import com.example.transformer.transformers.TransformerChain
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

// TODO: this should be moved to a common package since all the configuration should have the same format
@ConfigurationProperties
data class TransformerConfig(
    val outputTransformers: Map<String, List<String>>,
    val inputTransformers: Map<String, List<String>>
)

@Configuration
class TransformerConfiguration {
    @Value("classpath:slack-transformer-config.yaml")
    private lateinit var configFile: Resource

    @Bean
    fun transformerConfig(): TransformerConfig {
        val mapper = ObjectMapper(YAMLFactory()).apply {
            registerModule(KotlinModule.Builder().build())
        }

        return mapper.readValue(configFile.inputStream, TransformerConfig::class.java)
    }
}

@Configuration
class SlackTransformerChainConfig(
    private val transformerConfig: TransformerConfig,
    private val applicationContext: ApplicationContext,
) {
    fun <I, O> createOutputTransformerChainFromConfig(actionType: ActionType): TransformerChain<I, O> {
        val transformers = transformerConfig.outputTransformers[actionType.name]?.map { name ->
            applicationContext.getBean(name, Transformer::class.java)
        }

        @Suppress("UNCHECKED_CAST")
        return TransformerChain<I, O>(transformers as List<Transformer<Any, Any>>)
    }
     fun <I, O> createInputTransformerChainFromConfig(actionType: ActionType): TransformerChain<I, O> {
        val transformers = transformerConfig.inputTransformers[actionType.name]?.map { name ->
            applicationContext.getBean(name, Transformer::class.java)
        }

        @Suppress("UNCHECKED_CAST")
        return TransformerChain<I, O>(transformers as List<Transformer<Any, Any>>)
    }
}