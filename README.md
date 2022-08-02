# Kommando
Kommando is a KotlinJVM command framework mainly targeted towards
[deck](https://github.com/SrGaabriel/deck), a [Guilded](https://www.guilded.gg/)
bot framework (Guilded is basically [better discord](https://www.guilded.gg/blog/guilded-top-10-features-standalone)).

[[Installation]](https://helightdev.gitbook.io/kommando/installation) \
[[GitBook Documentation]](https://helightdev.gitbook.io/kommando/) 

```kotlin
fun main() = runBlocking {
    val client = DeckClient("TOKEN")
    val manager = GuildedCommandManager(client)
    manager.registerClasses(ExampleCommands::class.java)
    client.login()
}

@Command("bot")
object ExampleCommands {
    
    @Command("test")
    @Scope(GuildedServerChatScope::class)
    fun test(@Full message: String) = command {
        "Received '$message'!"
    }
    
}
```