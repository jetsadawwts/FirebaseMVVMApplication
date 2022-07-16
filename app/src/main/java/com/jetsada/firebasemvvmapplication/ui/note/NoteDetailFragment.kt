package com.jetsada.firebasemvvmapplication.ui.note

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jetsada.firebasemvvmapplication.databinding.NoteDetailFragmentBinding
import com.jetsada.firebasemvvmapplication.model.Note
import com.jetsada.firebasemvvmapplication.util.UiState
import com.jetsada.firebasemvvmapplication.util.hide
import com.jetsada.firebasemvvmapplication.util.show
import com.jetsada.firebasemvvmapplication.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

    companion object {
        fun newInstance() = NoteDetailFragment()
    }
    val TAG: String = "NoteDetailFragment"
    lateinit var binding: NoteDetailFragmentBinding
    private lateinit var viewModel: NoteViewModel
    var isEdit = false
    var objNote: Note? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = NoteDetailFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        binding.button.setOnClickListener {
           if(isEdit) {
               updateNote()
           } else {
               createNote()
           }
        }
    }

    private fun updateNote() {
        if(validation()){
            viewModel.updateNote(
                Note(
                    id = objNote?.id ?: "",
                    text = binding.noteMsg.text.toString(),
                    date = Date()
                )
            )
        }
        viewModel.updateNote.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.btnProgressAr.show()
                    binding.button.text = ""
                }
                is UiState.Failure -> {
                    binding.btnProgressAr.hide()
                    binding.button.text = "Update"
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.btnProgressAr.hide()
                    binding.button.text = "Update"
                    toast(state.data)
                }
            }
        }
    }

    private fun createNote() {
        if(validation()){
            viewModel.addNote(
                Note(
                    id = "",
                    text = binding.noteMsg.text.toString(),
                    date = Date()
                )
            )
        }
        viewModel.addNote.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.btnProgressAr.show()
                    binding.button.text = ""
                }
                is UiState.Failure -> {
                    binding.btnProgressAr.hide()
                    binding.button.text = "Craete"
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.btnProgressAr.hide()
                    binding.button.text = "Craete"
                    binding.noteMsg.setText("")
                    toast(state.data)
                }
            }
        }
    }

    private fun updateUi() {
        val type = arguments?.getString("type",null)
        objNote = arguments?.getParcelable("note")
        type?.let {
            when(it) {
                "view" -> {
                    isEdit = false
                    binding.noteMsg.isEnabled = false
                    binding.noteMsg.setText(objNote?.text)
                    binding.button.hide()
                }
                "create" -> {
                    isEdit = false
                    binding.noteMsg.isEnabled = true
                    binding.button.text = "Craete"
                }
                "edit" -> {
                    isEdit = true
                    binding.noteMsg.setText(objNote?.text)
                    binding.button.text = "Update"
                }
            }
        }

    }


    private fun validation(): Boolean {
        var isValid = true
        if(binding.noteMsg.text.toString().isEmpty()){
            isValid = false
            toast("Enter Message")
        }
        return isValid
    }


}