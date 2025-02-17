package com.example.transformers.slack.send_messaage


import com.example.transformers.Transformer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component("slackWebRequestPreprocessor")
class SlackWebRequestPreprocessor(
    private val webClient: WebClient
) : Transformer<String, String?> {
    override fun transform(input: String): String? {
//        TODO: could do the transformation of the header and how to send the request here
        println("get input for the web processor input: $input")
        val response = webClient.post().uri("/post").bodyValue(input).retrieve().bodyToMono(String::class.java).block()
        println("response from webclient: $response")

        return response
    }
}

