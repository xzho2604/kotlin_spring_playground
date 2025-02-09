package com.example.controller

import com.example.transformer.transformers.MessageService
import com.example.transformer.transformers.ValidatedSlackMessage
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val messageService: MessageService,
) {
    @PostMapping("/{actionType}")
    fun sendSlackMessage(
        @PathVariable actionType: String,
        @RequestBody message: String
    ): ValidatedSlackMessage {
        print(actionType)
        return messageService.processSlackMessage(message)
    }
}
