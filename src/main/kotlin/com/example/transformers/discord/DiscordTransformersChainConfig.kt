package com.example.transformers.discord

import com.example.transformers.ActionType
import com.example.transformers.BaseTransformerChainConfig
import com.example.transformers.transformerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource


@Configuration
class DiscordTransformersChainConfig(
    @Value("classpath:discord-transformer-config.yaml")
    private val configResource: Resource,
    applicationContext: ApplicationContext,
) : BaseTransformerChainConfig(transformerConfig(configResource), applicationContext) {
    override val provider = "discord"
    override val actionType = ActionType.discord_send_message
}