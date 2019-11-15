package by.bsu.ddzina.lab1

class Rule(private val conditions: Map<String, Any>, val target: Pair<String, String>) {

    private val prettyString: String

    init {
        prettyString = "if " + conditions.map {
            "${it.key}=${it.value}"
        }.joinToString(" and ") + " then ${target.first}=${target.second}"
    }

    fun toPrettyString(): String {
        return prettyString
    }

    fun isRuleForConditions(possibleConditions: Map<String, Any>): Boolean {
        return conditions == possibleConditions
    }

    fun satisfies(attribute: String, value: Any?): Boolean {
        return (conditions.containsKey(attribute) == false && target.first != attribute) ||
                (value != null && conditions[attribute] == value) ||
                (target.first == attribute && target.second == value as? String)
    }
}