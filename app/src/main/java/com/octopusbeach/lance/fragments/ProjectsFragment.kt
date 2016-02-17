package com.octopusbeach.lance.fragments

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.client.AuthData
import com.firebase.client.Firebase
import com.octopusbeach.lance.BaseApplication
import com.octopusbeach.lance.R
import com.octopusbeach.lance.activities.CreateProjectActivity
import com.octopusbeach.lance.adapters.ProjectsAdapter

/**
 * Created by hudson on 2/11/16.
 */
class ProjectFragment : Fragment() {
    companion object {
        val ID = 1
    }
    val CREATE_NEW_PROJECT_REQUEST = 1
    val TAG = ProjectFragment::class.java.toString()
    var recycleView:RecyclerView? = null
    var adapter:ProjectsAdapter? = null

    fun newInstance(): ProjectFragment {
        val fragment = ProjectFragment()
        val args: Bundle? = Bundle()
        fragment.arguments = args
        return fragment
    }

    override fun onStop() {
        super.onStop()
        val adapter: ProjectsAdapter = recycleView?.adapter as ProjectsAdapter
        adapter.stopListening()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_projects, container, false)
        view.findViewById(R.id.btn_new_project)?.setOnClickListener { newProject() }

        //create our recyclerview
        recycleView = view.findViewById(R.id.project_list) as RecyclerView
        recycleView?.layoutManager = LinearLayoutManager(activity)

        initAdapter()
        adapter?.startListening()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_NEW_PROJECT_REQUEST) {
            if (resultCode == Activity.RESULT_OK){
                // since a new project was added we have to requery data.
                adapter?.clearData()
                adapter?.startListening()
            }

        }
    }

    private fun initAdapter() {
        val ref = Firebase(BaseApplication.FIREBASE_ROOT)
        val authData: AuthData? = ref.auth
        val projectRef = ref.child("projects/" + authData?.uid)
        adapter = ProjectsAdapter(projectRef)
        recycleView?.adapter =  adapter
    }

    private fun newProject() = startActivityForResult(Intent(activity, CreateProjectActivity::class.java), CREATE_NEW_PROJECT_REQUEST)

}
