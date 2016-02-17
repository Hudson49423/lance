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
    val START_PREFIX = "Started "

    inner class ProjectViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        var cv: CardView? = null
        var title: TextView? = null
        var description: TextView? = null
        var started: TextView? = null

        constructor(itemView: View?) : super(itemView) {
            cv = itemView?.findViewById(R.id.project_card) as CardView?
            title = itemView?.findViewById(R.id.title) as TextView?
            description = itemView?.findViewById(R.id.description) as TextView?
            started = itemView?.findViewById(R.id.start) as TextView?
            itemView?.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val key = keys[adapterPosition]
            // start up our ProjectActivity
        }

    }

    val keys = ArrayList<String>()
    val projects = ArrayList<Project>()
    var ref: Firebase? = null
    var listener: ChildEventListener? = null

    constructor(projectRef: Firebase?) {
        // important to remember that all of this is happening on a thread.
        ref = projectRef
        listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot?, previousChildName: String?) {
                val key = snapshot?.key ?: "-1"
                var index = 0
                if (previousChildName != null) index = keys.indexOf(previousChildName)
                keys.add(index, key)
                projects.add(index, getProjectForSnapShot(snapshot))
                notifyItemInserted(index)
            }

            override fun onChildRemoved(snapshot: DataSnapshot?) {
                val index: Int = keys.indexOf(snapshot?.key)
                keys.removeAt(index)
                projects.removeAt(index)
                notifyItemRemoved(index)
            }

            override fun onChildChanged(snapshot: DataSnapshot?, s: String?) {
                val index = keys.indexOf(snapshot?.key)
                projects[index] = getProjectForSnapShot(snapshot)
                notifyItemChanged(index)
            }

            override fun onCancelled(p0: FirebaseError?) {
                Log.e(TAG, "On cancelled called.")
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                Log.e(TAG, "On childed moved called.")
            }
        }
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

    fun startListening() {
        ref?.addChildEventListener(listener)
    }

    fun stopListening() {
        Log.d(TAG, "stopping")
        ref?.removeEventListener(listener)
    }

    fun clearData() {
        projects.clear()
        keys.clear()
        notifyDataSetChanged()
    }

    fun getProjectForSnapShot(snapshot:DataSnapshot?): Project {
        val title: String = snapshot?.child(TITLE)?.value as String
        val description: String = snapshot?.child(DESCRIPTION)?.value as String
        val start: String = START_PREFIX + snapshot?.child(START)?.value as String
        val end: String = snapshot?.child(END)?.value as String
        val wage: Double = snapshot?.child(WAGE)?.value as Double
        val budget: Double = snapshot?.child(BUDGET)?.value as Double
        return Project(title, description, start, end, wage, budget)
    }

}
