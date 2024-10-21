package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class GreetingController {
    @GetMapping("/{name}")
    fun retrieveGreeting(
        @PathVariable("name") name: String,
    ): String = "Hello $name you are cool!"
}
