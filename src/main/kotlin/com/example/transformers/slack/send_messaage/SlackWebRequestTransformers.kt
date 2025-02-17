package com.example.transformers.slack.send_messaage


import com.example.transformers.Transformer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component("slackWebRequestPreprocessor")
class SlackWebRequestPreprocessor(
    private val webClient: WebClient
) : Transformer<String, String?> {
    override fun transform(input: String): String? {
        // Transform raw input to Slack message DTO
        println("get input for the web processor input: $input")
        var response = webClient.get().uri("/get").retrieve().bodyToMono(String::class.java).block()
        println("response from webclient: $response")

        return response
    }
}

