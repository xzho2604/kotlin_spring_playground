package com.example.transformer.transformers.slack.send_messaage


// Data classes for the transformation steps
data class SlackMessageDTO(val content: String, val channel: String)
data class FormattedSlackMessage(val dto: SlackMessageDTO)
data class ValidatedSlackMessage(val formatted: FormattedSlackMessage)

