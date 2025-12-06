package id.kotlin.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.ext.tool.SayToUser
import ai.koog.agents.features.eventHandler.feature.handleEvents
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import kotlinx.coroutines.runBlocking

const val apiKey = ""

val agent = AIAgent(
    promptExecutor = simpleGoogleAIExecutor(apiKey),
    systemPrompt = "You are a helpful assistant that tells the current news in Indonesia",
    llmModel = GoogleModels.Gemini2_5Flash,
    temperature = 0.7,
    toolRegistry = ToolRegistry {
        tool(SayToUser)
    },
    maxIterations = 100
) {
    handleEvents {
        // Handle tool calls
        onToolCallStarting { eventContext ->
            println("Tool called: ${eventContext.tool.name} with args ${eventContext.toolArgs}")
        }
        // Handle event triggered when the agent completes its execution
        onAgentCompleted { eventContext ->
            println("Agent finished with result: ${eventContext.result}")
        }
    }
}

fun main() = runBlocking {
    val result = agent.run("Tells the current news in Indonesia")
    println(result)
}