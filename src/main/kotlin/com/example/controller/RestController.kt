package com.example.controller

import com.example.transformers.ActionType
import com.example.transformers.TransformerFactoryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val transformerFactoryService: TransformerFactoryService
) {
    @PostMapping("/{actionType}/{provider}")
    fun sendSlackMessage(
        @PathVariable actionType: ActionType,
        @PathVariable provider: String,
        @RequestBody message: String
    ): Any {

        // we passed in the identifier to find which transformer chain to use and this could use and action could use the actionId
        val inputTransformer = transformerFactoryService.getInputTransformerChain<Any, Any>(provider, actionType)
        val outputTransformer = transformerFactoryService.getOutputTransformerChain<Any, Any>(provider, actionType)
        println("executing ${actionType} input chain")
        println(inputTransformer.execute(message))
        println("executing slack output chain")

        return outputTransformer.execute(message)
    }
}
