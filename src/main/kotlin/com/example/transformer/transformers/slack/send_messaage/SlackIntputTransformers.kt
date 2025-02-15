package com.example.transformer.transformers.slack.send_messaage


import com.example.transformer.transformers.Transformer
import org.springframework.stereotype.Component


@Component("slackInputPreprocessor")
class SlackInputPreprocessor : Transformer<String, SlackMessageDTO> {
    override fun transform(input: String): SlackMessageDTO {
        // Transform raw input to Slack message DTO
        return SlackMessageDTO(input)
    }
}


@Component("slackInputFormatter")
class SlackInputFormatter : Transformer<SlackMessageDTO, FormattedSlackMessage> {
    override fun transform(input: SlackMessageDTO): FormattedSlackMessage {
        // Format the Slack message
        return FormattedSlackMessage(input)
    }
}

@Component("slackInputValidator")
class SlackInputValidator : Transformer<FormattedSlackMessage, ValidatedSlackMessage> {
    override fun transform(input: FormattedSlackMessage): ValidatedSlackMessage {
        // Validate the formatted message
        return ValidatedSlackMessage(input)
    }
}
