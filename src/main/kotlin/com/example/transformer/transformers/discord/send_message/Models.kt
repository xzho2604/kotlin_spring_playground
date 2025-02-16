package com.example.transformer.transformers.discord.send_message


// Data classes for the transformation steps
data class DiscordMessageDTO(val content: String, val channel: String)
data class FormattedDiscordMessage(val dto: DiscordMessageDTO)
data class ValidatedDiscordMessage(val formatted: FormattedDiscordMessage)

