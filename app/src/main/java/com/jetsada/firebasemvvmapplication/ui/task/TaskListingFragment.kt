package com.jetsada.firebasemvvmapplication.ui.task

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsada.firebasemvvmapplication.R
import com.jetsada.firebasemvvmapplication.data.model.Task
import com.jetsada.firebasemvvmapplication.databinding.FragmentTaskListingBinding
import com.jetsada.firebasemvvmapplication.ui.auth.AuthViewModel
import com.jetsada.firebasemvvmapplication.util.UiState
import com.jetsada.firebasemvvmapplication.util.hide
import com.jetsada.firebasemvvmapplication.util.show
import com.jetsada.firebasemvvmapplication.util.toast
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
@AndroidEntryPoint
class TaskListingFragment : Fragment() {

    val TAG: String = "TaskListingFragment"
    private var param1: String? = null
    val viewModel: TaskViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()
    lateinit var binding: FragmentTaskListingBinding
    val adapter by lazy {
        TaskListingAdapter(
            onDeleteClicked = { pos, item ->

            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (this::binding.isInitialized){
            return binding.root
        }else {
            binding = FragmentTaskListingBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addTaskButton.setOnClickListener {
            val createTaskFragmentSheet = CreateTaskFragment()
            createTaskFragmentSheet.show(childFragmentManager,"create_task")
        }

        binding.taskListing.layoutManager = LinearLayoutManager(requireContext())
        binding.taskListing.adapter = adapter

        authViewModel.gerSession {
            viewModel.getTask(it)
        }
        observer()
    }

    private fun observer() {
            viewModel.getTask.observe(viewLifecycleOwner) { state ->
                when(state) {
                    is UiState.Loading -> {
                        binding.progressBar.show()
                    }
                    is UiState.Success -> {
                        binding.progressBar.hide()
                        adapter.updateList(state.data.toMutableList())
                    }
                    is UiState.Failure -> {
                        binding.progressBar.hide()
                        toast(state.error)
                        Log.d("error", state.error.toString())
                    }
                }

            }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            TaskListingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}