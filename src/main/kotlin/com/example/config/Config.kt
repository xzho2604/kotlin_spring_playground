package com.example.config

import com.example.chain.*
import com.example.model.ApiResponse
import com.example.model.GithubResponse
import com.example.model.SlackResponse
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
@ConfigurationProperties(prefix = "api-configs")
data class ApiConfigurations(
    val slack: ApiConfig? = null,
    val github: ApiConfig? = null
)

data class ApiConfig(
    val baseUrl: String,
    val chainType: String,
    val headers: Map<String, String> = emptyMap()
)

@Configuration
class ChainConfigurations() {
    @Autowired
    private lateinit var registry: ResponseChainRegistry

    @Bean

    fun slackChain(): ResponseHandler<ApiResponse<SlackResponse>> {
        return NullCheckHandler<SlackResponse>().apply {
            setNext(SlackSpecificHandler())
            setNext(MetadataEnrichmentHandler())
        }
    }

    @Bean
    fun githubChain(): ResponseHandler<ApiResponse<GithubResponse>> {
        return NullCheckHandler<GithubResponse>().apply {
            setNext(GithubSpecificHandler())
            setNext(MetadataEnrichmentHandler())
        }
    }

    @PostConstruct
    fun registerChains() {
        registry.registerChain("slack", slackChain())
        registry.registerChain("github", githubChain())
    }
}

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)
            }
            .build()
    }
}