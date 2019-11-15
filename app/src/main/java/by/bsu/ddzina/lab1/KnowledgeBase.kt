package by.bsu.ddzina.lab1

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

private val baseFileName = "base.json"

object KnowledgeBase {

    private lateinit var baseJson: JSONArray
    private val rules = mutableListOf<Rule>()
    private lateinit var rulesString: List<String>

    fun init(context: Context) {
        val rawText = BufferedReader(InputStreamReader(context.assets.open(baseFileName))).readText()
        baseJson = JSONArray(rawText)
        parseRules()
    }

    private fun parseRules() {
        repeat(baseJson.length()) { ruleIndex ->
            val ruleJson = baseJson[ruleIndex] as JSONObject
            val conditionsJson = ruleJson["if"] as JSONArray
            val conditions = HashMap<String, Any>()
            repeat(conditionsJson.length()) { conditionIndex ->
                val conditionJson = conditionsJson[conditionIndex] as JSONObject
                val attr = conditionJson["attr"] as String
                val value = conditionJson["value"]
                conditions[attr] = value
            }
            val targetJson = ruleJson["then"] as JSONObject
            val targetAttr = targetJson["attr"] as String
            val targetValue = targetJson["value"] as String
            val target = Pair<String, String>(targetAttr, targetValue)
            rules.add(Rule(conditions, target))
        }

        rulesString = rules.map {
            it.toPrettyString()
        }
    }

    fun getRules(): List<Rule> {
        return rules
    }

    fun getRulesString(): List<String> {
        return rulesString
    }
}