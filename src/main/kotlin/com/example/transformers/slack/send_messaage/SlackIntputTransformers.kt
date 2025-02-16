package com.example.transformers.slack.send_messaage


import com.example.transformers.Transformer
import org.springframework.stereotype.Component


@Component("")
class SlackInputPreprocessor : Transformer<String, SlackMessageDTO> {
    override fun transform(input: String): SlackMessageDTO {
        // Transform raw input to Slack message DTO
        return SlackMessageDTO(input, "slack_general")
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
