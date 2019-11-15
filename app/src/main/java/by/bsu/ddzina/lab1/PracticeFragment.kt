package by.bsu.ddzina.lab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView.OnItemSelectedListener

class PracticeFragment : Fragment() {

    private val areas = arrayOf("не выбрано", "физика", "математика", "биология")
    private val times = arrayOf("не выбрано", "1630", "1700", "1750", "1850", "1930")
    private val countries = arrayOf("не выбрано", "Англия", "Германия", "Дания", "Франция", "Швейцария")
    private val usedRules = mutableListOf<Rule>()
    private lateinit var usedRulesAdapter: ArrayAdapter<String>
    private val currentConditions = HashMap<String, Any>()
    private val adapterToAttribute = mutableMapOf<Int, String>()
    private val attrToSpinnerAndData = mutableMapOf<String, Pair<Spinner, Array<String>>>()
    private val areaAttribute = "area"
    private val timeAttribute = "time"
    private val countryAttribute = "country"
    private val resultAttribute = "scientist"
    private lateinit var resultTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_practice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSpinner(R.id.areaSpinner, areas, areaAttribute)
        setupSpinner(R.id.timeSpinner, times, timeAttribute)
        setupSpinner(R.id.countrySpinner, countries, countryAttribute)

        val usedRulesListView = view.findViewById(R.id.usedRulesListView) as ListView
        usedRulesAdapter = ArrayAdapter(view.context, R.layout.row, R.id.rowTextView)
        usedRulesListView.adapter = usedRulesAdapter

        resultTextView = view.findViewById(R.id.resultTextView)
    }

    private fun setupSpinner(id: Int, data: Array<String>, attribute: String) {
        val adapter = ArrayAdapter<String>(view!!.context, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterToAttribute[id] = attribute

        val spinner = view!!.findViewById(id) as Spinner
        attrToSpinnerAndData[attribute] = Pair(spinner, data)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val chosenAttr = adapterToAttribute[parent.id]!!
                val chosenValue: Any?
                if (position == 0) {
                    currentConditions.remove(chosenAttr)
                    chosenValue = null
                } else {
                    chosenValue = parent.adapter.getItem(position)
                    currentConditions[chosenAttr] = chosenValue
                }
                removeUnusedRules(chosenAttr, chosenValue)
                handleConditionsChanged()
                usedRulesAdapter.clear()
                usedRulesAdapter.addAll(usedRules.map { it.toPrettyString() })
                usedRulesAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {

            }
        }
    }

    private fun removeUnusedRules(attribute: String, value: Any?) {
        usedRules.removeAll {
            it.satisfies(attribute, value) == false
        }
    }

    private fun handleConditionsChanged() {
        for (rule in KnowledgeBase.getRules()) {
            if (rule.isRuleForConditions(currentConditions)) {
                handleNewRule(rule)
                return
            }
        }
        onResultRemoved()
    }

    private fun handleNewRule(rule: Rule) {
        usedRules.add(rule)
        val targetAttr = rule.target.first
        if (targetAttr == resultAttribute) {
            onResultChanged(rule.target.second)
        } else {
            val spinnerAndData = attrToSpinnerAndData[targetAttr]!!
            spinnerAndData.first.setSelection(spinnerAndData.second.indexOf(rule.target.second))
        }
    }

    private fun onResultRemoved() {
        resultTextView.text = getString(R.string.undefined_result)
    }

    private fun onResultChanged(result: String) {
        resultTextView.text = "Учёный: $result"
    }
}