package com.example.controller

import com.example.model.User
import com.example.service.UserService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    suspend fun getUser(@PathVariable(value = "id") id: Long): User {
        return userService.getUser(id)
    }

    @GetMapping
    suspend fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }

    @PostMapping
    suspend fun createUser(@RequestBody user: User): User {
        return userService.createUser(user)
    }
}


@RestController
@RequestMapping("/api")
class GreetingController {

    @GetMapping("/greeting")
    suspend fun getGreeting(@RequestParam(value = "name", defaultValue = "World") name: String): Map<String, String> {
        return mapOf("message" to "Hello, $name!")
    }
}