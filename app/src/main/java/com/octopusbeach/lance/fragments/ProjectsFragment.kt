package com.octopusbeach.lance.fragments

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.octopusbeach.lance.R
import com.octopusbeach.lance.activities.CreateProjectActivity

/**
 * Created by hudson on 2/11/16.
 */
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
class ProjectFragment : Fragment() {
    companion object {
        val ID = 1
    }
    val CREATE_NEW_PROJECT_REQUEST = 1
    val TAG = ProjectFragment::class.java.toString()

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectFragment.
     */
    fun newInstance(): ProjectFragment {
        val fragment = ProjectFragment()
        val args: Bundle? = Bundle()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project, container, false)
        view.findViewById(R.id.btn_new_project)?.setOnClickListener { newProject() }
        return view
    }

    private fun newProject() = startActivityForResult(Intent(activity, CreateProjectActivity::class.java), CREATE_NEW_PROJECT_REQUEST)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_NEW_PROJECT_REQUEST) {
            if (resultCode == Activity.RESULT_OK){
                // should probably update views or something.
                Log.d(TAG, "received result ok")
            }

        }
    }


}
