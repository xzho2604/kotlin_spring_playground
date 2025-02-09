package com.example.controller

import com.example.transformer.transformers.ActionType
import com.example.transformer.transformers.slack.send_messaage.SlackOutputTransformers
import com.example.transformer.transformers.slack.send_messaage.ValidatedSlackMessage
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val slackOutputTransformers: SlackOutputTransformers
) {
    @PostMapping("/{actionType}")
    fun sendSlackMessage(
        @PathVariable actionType: ActionType,
        @RequestBody message: String
    ): ValidatedSlackMessage {

        print(actionType)
        val chain = slackOutputTransformers.getChain<Any, ValidatedSlackMessage>(actionType)

        return chain.execute(message)
    }
}
