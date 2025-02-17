package com.example.transformers.slack.send_messaage

import com.example.transformers.ActionType
import com.example.transformers.BaseTransformerChainConfig
import com.example.transformers.transformerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource


@Configuration
class SlackTransformersChainConfig(
    @Value("classpath:slack-transformer-config.yaml")
    private val configResource: Resource,
    applicationContext: ApplicationContext,
) : BaseTransformerChainConfig(transformerConfig(configResource), applicationContext) {
    override val provider = "slack"
    override val actionType = ActionType.SLACK_MESSAGE
}
