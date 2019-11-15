package by.bsu.ddzina.lab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class KnowledgeBaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_knowledge_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rulesListView = view.findViewById(R.id.rulesListView) as ListView
        val arrayAdapter = ArrayAdapter<String>(view.context, R.layout.row, R.id.rowTextView, KnowledgeBase.getRulesString())
        rulesListView.adapter = arrayAdapter
    }
}