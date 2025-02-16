package com.example.transformer.transformers.discord.send_message


import com.example.transformer.transformers.Transformer
import org.springframework.stereotype.Component


@Component("discordMessagePreprocessor")
class DiscordMessagePreprocessor : Transformer<String, DiscordMessageDTO> {
    override fun transform(input: String): DiscordMessageDTO {
        // Transform raw input to discord message DTO

        return DiscordMessageDTO(input, "discord_general")
    }
}


@Component("discordMessageFormatter")
class DiscordMessageFormatter : Transformer<DiscordMessageDTO, FormattedDiscordMessage> {
    override fun transform(input: DiscordMessageDTO): FormattedDiscordMessage {
        // Format the discord message
        return FormattedDiscordMessage(input)
    }
}

@Component("discordMessageValidator")
class DiscordMessageValidator : Transformer<FormattedDiscordMessage, ValidatedDiscordMessage> {
    override fun transform(input: FormattedDiscordMessage): ValidatedDiscordMessage {
        // Validate the formatted message
        return ValidatedDiscordMessage(input)
    }
}
