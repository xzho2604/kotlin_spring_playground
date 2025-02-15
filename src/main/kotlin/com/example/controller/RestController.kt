package com.example.controller

import com.example.transformer.transformers.ActionType
import com.example.transformer.transformers.TransformerFactoryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val transformerFactoryService: TransformerFactoryService
) {
    @PostMapping("/{actionType}")
    fun sendSlackMessage(
        @PathVariable actionType: ActionType,
        @RequestBody message: String
    ): Any {

//        val slackinputchain = slacktransformerchainconfig.createinputtransformerchainfromconfig<string, validatedslackmessage>(actiontype)
//        val slackoutputchain = slacktransformerchainconfig.createinputtransformerchainfromconfig<string, validatedslackmessage>(actiontype)
//        println("executing slack input chain")
//        println(slackinputchain.execute(message))
//        println("executing slack output chain")
//
        // we passed in the identifier to find which transformer chain to use and this could use and action could use the actionId
        val inputTransformer = transformerFactoryService.getInputTransformerChain<Any, Any>("slack", actionType)
        val outputTransformer = transformerFactoryService.getOutputTransformerChain<Any, Any>("slack", actionType)
        println("executing ${actionType} input chain")
        println(inputTransformer.execute(message))
        println("executing slack output chain")

        return outputTransformer.execute(message)
    }
}
