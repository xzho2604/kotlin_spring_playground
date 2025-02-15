package com.example.transformer.transformers.slack.send_messaage

import com.example.transformer.transformers.ActionType
import com.example.transformer.transformers.Transformer
import com.example.transformer.transformers.TransformerChain
import org.springframework.context.ApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.Yaml



@Service
class SlackTransformerChainConfig(
    private val transformerConfig: TransformerConfig,
    private val applicationContext: ApplicationContext
) {
    fun <I, O> createChainFromConfig(actionType: ActionType): TransformerChain<I, O> {
        println(transformerConfig)

        val transformers = transformerConfig.outputTransformers[actionType.name]?.map { name ->
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