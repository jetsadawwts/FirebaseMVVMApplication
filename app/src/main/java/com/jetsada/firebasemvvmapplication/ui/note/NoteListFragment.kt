package com.jetsada.firebasemvvmapplication.ui.note

import android.app.ProgressDialog.show
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
import com.jetsada.firebasemvvmapplication.R
import com.jetsada.firebasemvvmapplication.databinding.NoteListFragmentBinding
import com.jetsada.firebasemvvmapplication.util.UiState
import com.jetsada.firebasemvvmapplication.util.hide
import com.jetsada.firebasemvvmapplication.util.show
import com.jetsada.firebasemvvmapplication.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    companion object {
        fun newInstance() = NoteListFragment()
    }

    val TAG: String = "NoteListFragment"
    lateinit var binding: NoteListFragmentBinding
    private lateinit var viewModel: NoteViewModel
    val noteViewModel: NoteViewModel by viewModels()
    val adapter by lazy {
        NoteListAdapter(
            onItemClicked = { pos, item ->
                findNavController().navigate(R.id.action_noteListFragment_to_noteDetailFragment, Bundle().apply{
                    putString("type", "view")
                    putParcelable("note", item)
                })
            },
            onEditClicked = { pos, item ->
                findNavController().navigate(R.id.action_noteListFragment_to_noteDetailFragment, Bundle().apply{
                    putString("type", "edit")
                    putParcelable("note", item)
                })
            },
            onDeleteClicked = { pos, item ->
                findNavController().navigate(R.id.action_noteListFragment_to_noteDetailFragment, Bundle().apply{
                    putString("type", "delete")
                })
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = NoteListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_noteDetailFragment, Bundle().apply {
                putString("type", "create")
            })
        }
        viewModel.getNotes()
        viewModel.note.observe(viewLifecycleOwner, Observer { state ->
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
                  adapter.updateList(state.data.toMutableList())
//                  state.data.forEach {
//                      Log.d(TAG, it.toString())
//                  }
              }
          }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}