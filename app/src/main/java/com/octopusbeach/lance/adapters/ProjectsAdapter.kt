package com.octopusbeach.lance.adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.firebase.client.ChildEventListener
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.octopusbeach.lance.R
import com.octopusbeach.successtrack.model.Project
import java.util.*

/**
 * Created by hudson on 2/11/16.
 */

class ProjectsAdapter : RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {
    val TAG = "Projects Adapter"

    val TITLE = "title"
    val DESCRIPTION = "description"
    val WAGE = "wage"
    val BUDGET = "budget"
    val START = "start"
    val END = "end"

    inner class ProjectViewHolder : RecyclerView.ViewHolder {
        var cv: CardView? = null
        var title: TextView? = null
        var description: TextView? = null
        var started: TextView? = null

        constructor(itemView: View?) : super(itemView) {
            cv = itemView?.findViewById(R.id.project_card) as CardView?
            title = itemView?.findViewById(R.id.title) as TextView?
            description = itemView?.findViewById(R.id.description) as TextView?
            started = itemView?.findViewById(R.id.start) as TextView?
        }
    }

    val keys = ArrayList<String>()
    val projects = ArrayList<Project>()
    var ref: Firebase? = null
    var listener: ChildEventListener? = null

    constructor(projectRef: Firebase?) {
        // important to remember that all of this is happening on a thread.
        ref = projectRef
        listener = projectRef?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot?, previousChildName: String?) {
                val title: String = snapshot?.child(TITLE)?.value as String
                val description: String = snapshot?.child(DESCRIPTION)?.value as String
                val start: String = snapshot?.child(START)?.value as String
                val end: String = snapshot?.child(END)?.value as String
                val wage: Double = snapshot?.child(WAGE)?.value as Double
                val budget: Double = snapshot?.child(BUDGET)?.value as Double
                val project = Project(title, description, start, end, wage, budget)

                val key = snapshot?.key ?: "-1"
                if (previousChildName == null) {
                    projects.add(0, project)
                    keys.add(0, key)
                } else {
                    // Note that this reverses the order of the projects.
                    val index = keys.indexOf(previousChildName)
                    keys.add(index, key)
                    projects.add(index, project)
                }
                notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot?) {
                val index: Int = keys.indexOf(snapshot?.key)
                keys.removeAt(index)
                projects.removeAt(index)
                notifyDataSetChanged()
            }

            override fun onCancelled(p0: FirebaseError?) {
                Log.e(TAG, "On cancelled called.")
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                Log.e(TAG, "On child changed called.")
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                Log.e(TAG, "On childed moved called.")
            }
        })
    }

    override fun getItemCount() = projects.size

    override fun onCreateViewHolder(viewGroup: ViewGroup?, position: Int): ProjectViewHolder? {
        val v = LayoutInflater.from(viewGroup?.context).inflate(R.layout.project_card,
                viewGroup, false)
        val holder = ProjectViewHolder(v)
        return holder
    }

    override fun onBindViewHolder(holder: ProjectViewHolder?, i: Int) {
        val project = projects[i]
        holder?.title?.text = project.title
        holder?.description?.text = project.description
        holder?.started?.text = project.start
    }

    fun stopListening() {
        Log.d(TAG, "stopping")
        ref?.removeEventListener(listener)
        // remove the data
        projects.clear()
        keys.clear()
    }

}
