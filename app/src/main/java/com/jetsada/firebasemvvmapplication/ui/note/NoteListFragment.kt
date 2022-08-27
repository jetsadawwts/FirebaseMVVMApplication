package com.jetsada.firebasemvvmapplication.ui.note

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jetsada.firebasemvvmapplication.R
import com.jetsada.firebasemvvmapplication.databinding.NoteListFragmentBinding
import com.jetsada.firebasemvvmapplication.data.model.Note
import com.jetsada.firebasemvvmapplication.ui.auth.AuthViewModel
import com.jetsada.firebasemvvmapplication.util.UiState
import com.jetsada.firebasemvvmapplication.util.hide
import com.jetsada.firebasemvvmapplication.util.show
import com.jetsada.firebasemvvmapplication.util.toast
import dagger.hilt.android.AndroidEntryPoint


private const val ARG_PARAM1 = "param1"


@AndroidEntryPoint
class NoteListFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            NoteListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    val TAG: String = "NoteListFragment"
    var param1: String? = null
    lateinit var binding: NoteListFragmentBinding
    val noteViewModel: NoteViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()
    var list: MutableList<Note> = arrayListOf()
    val adapter by lazy {
        NoteListAdapter(
            onItemClicked = { pos, item ->
                findNavController().navigate(R.id.action_noteListingFragment_to_noteDetailFragment, Bundle().apply {
                    putString("type", "view")
                    putParcelable("note", item)
                })
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (this::binding.isInitialized){
            return binding.root
        }else {
            binding = NoteListFragmentBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = staggeredGridLayoutManager
        binding.recyclerView.adapter = adapter
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_noteListingFragment_to_noteDetailFragment)
        }

        authViewModel.gerSession {
            noteViewModel.getNotes(it)
        }
    }

    private fun observe() {
        noteViewModel.note.observe(viewLifecycleOwner, Observer { state ->
            when(state) {
                is UiState.Loading -> {
                    Log.e(TAG, "Loading")
                    binding.progressBar.show()
                }

                is UiState.Failure -> {
                    Log.e(TAG, state.error.toString())
                    binding.progressBar.hide()
                    toast(state.error)
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    list = state.data.toMutableList()
                    adapter.updateList(list)
                }
            }
        })
    }


}