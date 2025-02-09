package com.example.transformer.transformers.slack.send_messaage

import com.example.transformer.transformers.Transformer
import com.example.transformer.transformers.TransformerChain
import org.springframework.context.ApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.yaml.snakeyaml.Yaml

class SlackInputTransformers {

}



// Configuration data classes
data class TransformerConfig(
    val outputTransformers: Map<String, List<String>>
)

// Factory to create transformer chains from configuration
//TODO fix load the yaml into class using spring qualifier
@Component
class TransformerChainFactory(
    private val applicationContext: ApplicationContext
) {
    fun <I, O> createChainFromConfig(actionType: String): TransformerChain<I, O> {
        val config = loadConfig()
        val transformerNames = config.outputTransformers[actionType]
            ?: throw IllegalArgumentException("No transformer chain configured for: $actionType")

        val transformers = transformerNames.map { name ->
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