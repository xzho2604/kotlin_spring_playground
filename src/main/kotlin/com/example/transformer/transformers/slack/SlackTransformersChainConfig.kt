package com.example.transformer.transformers.slack

import com.example.transformer.transformers.ActionType
import com.example.transformer.transformers.BaseTransformerChainConfig
import com.example.transformer.transformers.BaseTransformerConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource


@Configuration
class SlackTransformersChainConfig(
    @Value("classpath:slack-transformer-config.yaml")
    private val configFile: Resource
) : BaseTransformerConfiguration(configFile) {}


@Configuration
class SlackTransformerChainConfig(
    transformerConfig: com.example.transformer.transformers.TransformerConfig,
    applicationContext: ApplicationContext,
) : BaseTransformerChainConfig(transformerConfig, applicationContext) {
    override val provider = "slack"
    override val actionType = ActionType.SLACK_MESSAGE
}