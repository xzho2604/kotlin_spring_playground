@file:Suppress("ktlint:standard:no-wildcard-imports")

package controller

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.time.Instant
import java.util.*

@Controller
class GraphqlController {
    private val users = mutableMapOf<String, User>()

    @QueryMapping
    fun hello(): String = "Hello, GraphQL!"

    @QueryMapping
    fun greeting(
        @Argument name: String?,
    ): String = "Hello, ${name ?: "World"}!"

    @QueryMapping
    fun user(
        @Argument id: String,
    ): User? = users[id]

    @QueryMapping
    fun allUsers(): List<User> = users.values.toList()

    @MutationMapping
    fun saveGreeting(
        @Argument message: String,
    ): Boolean {
        println("Saving greeting: $message")
        return true
    }

    @MutationMapping
    fun createUser(
        @Argument input: UserInput,
    ): User {
        val id = UUID.randomUUID().toString()
        val user =
            User(
                id = id,
                name = input.name,
                email = input.email,
                profile =
                    Profile(
                        bio = input.bio,
                        avatar = input.avatar,
                        joinDate = Instant.now().toString(),
                    ),
                posts = emptyList(),
            )
        users[id] = user
        return user
    }

    @MutationMapping
    fun updateUser(
        @Argument id: String,
        @Argument input: UserInput,
    ): User? =
        users[id]?.also { existingUser ->
            users[id] =
                existingUser.copy(
                    name = input.name,
                    email = input.email,
                    profile =
                        existingUser.profile.copy(
                            bio = input.bio,
                            avatar = input.avatar,
                        ),
                )
        }
}

// Data classes for our schema
data class User(
    val id: String,
    val name: String,
    val email: String,
    val profile: Profile,
    val posts: List<Post>,
)

data class Profile(
    val bio: String?,
    val avatar: String?,
    val joinDate: String,
)

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val author: User,
    val createdAt: String,
)

data class UserInput(
    val name: String,
    val email: String,
    val bio: String?,
    val avatar: String?,
)
