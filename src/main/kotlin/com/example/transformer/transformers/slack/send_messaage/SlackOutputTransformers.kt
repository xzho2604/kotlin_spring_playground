package com.example.transformer.transformers.slack.send_messaage


import com.example.transformer.transformers.ActionType
import com.example.transformer.transformers.Transformer
import com.example.transformer.transformers.TransformerChain
import org.springframework.stereotype.Component

// Updated SlackOutputTransformers using configuration
@Component
class SlackOutputTransformers(
    private val transformerChainFactory: TransformerChainFactory
) {
    val identifier: ActionType = ActionType.SLACK_MESSAGE

    fun <I, O> getChain(type: ActionType): TransformerChain<I, O> {
        return transformerChainFactory.createChainFromConfig(type.name)
    }
}


@Component("slackMessagePreprocessor")
class SlackMessagePreprocessor : Transformer<String, SlackMessageDTO> {
    override fun transform(input: String): SlackMessageDTO {
        // Transform raw input to Slack message DTO
        return SlackMessageDTO(input)
    }
}


@Component("slackMessageFormatter")
class SlackMessageFormatter : Transformer<SlackMessageDTO, FormattedSlackMessage> {
    override fun transform(input: SlackMessageDTO): FormattedSlackMessage {
        // Format the Slack message
        return FormattedSlackMessage(input)
    }
}

@Component("slackMessageValidator")
class SlackMessageValidator : Transformer<FormattedSlackMessage, ValidatedSlackMessage> {
    override fun transform(input: FormattedSlackMessage): ValidatedSlackMessage {
        // Validate the formatted message
        return ValidatedSlackMessage(input)
    }
}
