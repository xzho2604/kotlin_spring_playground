package com.example.transformer.transformers.slack.send_messaage

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
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.yaml.snakeyaml.Yaml

// Factory to create transformer chains from configuration
//TODO fix load the yaml into class using spring qualifier
@Component
class TransformerChainFactory(
    private val applicationContext: ApplicationContext
) {
    fun <I, O> createChainFromConfig(actionType: String): TransformerChain<I, O> {
//        val config = loadConfig()
//        val transformerNames = config.outputTransformers[actionType]
//            ?: throw IllegalArgumentException("No transformer chain configured for: $actionType")

        val yaml = Yaml()
        val inputStream = ClassPathResource("transformer-config.yaml").inputStream
        val transformerConfig = yaml.loadAs(inputStream, TransformerConfig::class.java)
        println(transformerConfig)

//        this is working just
        val transformers = listOf<String>("slackMessagePreprocessor", "slackMessageFormatter").map { name ->
            applicationContext.getBean(name, Transformer::class.java)
        }

        @Suppress("UNCHECKED_CAST")
        return TransformerChain<I, O>(transformers as List<Transformer<Any, Any>>)
    }

    private fun loadConfig(): TransformerConfig {
        val yaml = Yaml()
        val inputStream = ClassPathResource("transformer-config.yaml").inputStream
        return yaml.loadAs(inputStream, TransformerConfig::class.java)
    }
}




@ConfigurationProperties
data class TransformerConfig(
    val outputTransformers: Map<String, List<String>>
)

@Configuration
class TransformerConfiguration {

    @Value("classpath:transformer-config.yaml")
    private lateinit var configFile: Resource

    @Bean
    fun transformerConfig(): TransformerConfig {
        val mapper = ObjectMapper(YAMLFactory()).apply {
            registerModule(KotlinModule.Builder().build())
        }

        return mapper.readValue(configFile.inputStream, TransformerConfig::class.java)
    }
}