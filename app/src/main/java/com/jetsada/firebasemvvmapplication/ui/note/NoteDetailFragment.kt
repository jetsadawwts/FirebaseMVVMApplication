package com.jetsada.firebasemvvmapplication.ui.note

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.jetsada.firebasemvvmapplication.R
import com.jetsada.firebasemvvmapplication.databinding.NoteDetailFragmentBinding
import com.jetsada.firebasemvvmapplication.data.model.Note
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.jetsada.firebasemvvmapplication.ui.auth.AuthViewModel
import com.jetsada.firebasemvvmapplication.util.*
import java.text.SimpleDateFormat
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import java.util.*

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

    companion object {
        fun newInstance() = NoteDetailFragment()
    }
    val TAG: String = "NoteDetailFragment"
    lateinit var binding: NoteDetailFragmentBinding
    private lateinit var viewModel: NoteViewModel
    val authViewModel: AuthViewModel by viewModels()
    var isEdit = false
    var objNote: Note? = null
    var tagsList: MutableList<String> = arrayListOf()
    var imageUris: MutableList<Uri> = arrayListOf()
    val adapter by lazy {
        ImageListingAdapter(
            onCancelClicked = { pos, item -> onRemoveImage(pos, item) }
        )
    }

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data!!
            imageUris.add(fileUri)
            adapter.updateList(imageUris)
            binding.progressBar.hide()
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            binding.progressBar.hide()
            toast(ImagePicker.getError(data))
        } else {
            binding.progressBar.hide()
            Log.e(TAG,"Task Cancelled")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (this::binding.isInitialized){
            return binding.root
        }else {
            binding = NoteDetailFragmentBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        observer()
    }
    private fun observer() {
        viewModel.addNote.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    toast(state.data.second)
                    objNote = state.data.first
                    isMakeEnableUI(false)
                    binding.done.hide()
                    binding.delete.show()
                    binding.edit.show()
                }
            }
        }
        viewModel.updateNote.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    toast(state.data)
                    binding.done.hide()
                    binding.edit.show()
                    isMakeEnableUI(false)
                }
            }
        }

        viewModel.deleteNote.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    toast(state.data)
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun updateUI() {
        val sdf = SimpleDateFormat("dd MMM yyyy . hh:mm a")
        objNote = arguments?.getParcelable("note")
        binding.tags.layoutParams.height = 40.dpToPx
        objNote?.let { note ->
            binding.title.setText(note.title)
            binding.date.setText(sdf.format(note.date))
            tagsList = note.tags
            addTags(tagsList)
            binding.description.setText(note.description)
            binding.done.hide()
            binding.edit.show()
            binding.delete.show()
            isMakeEnableUI(false)
        } ?: run {
            binding.title.setText("")
            binding.date.setText(sdf.format(Date()))
            binding.description.setText("")
            binding.done.hide()
            binding.edit.hide()
            binding.delete.hide()
            isMakeEnableUI(true)
        }
        binding.images.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.images.adapter = adapter
        binding.images.itemAnimator = null
        binding.addImageLl.setOnClickListener {
            binding.progressBar.show()
            ImagePicker.with(this)
                //.crop()
                .compress(1024)
                .galleryOnly()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.title.setOnClickListener {
            isMakeEnableUI(true)
        }
        binding.description.setOnClickListener {
            isMakeEnableUI(true)
        }
        binding.delete.setOnClickListener {
            objNote?.let { viewModel.deleteNote(it) }
        }
        binding.addTagLl.setOnClickListener {
            showAddTagDialog()
        }
        binding.edit.setOnClickListener {
            isMakeEnableUI(true)
            binding.done.show()
            binding.edit.hide()
            binding.title.requestFocus()
        }
        binding.done.setOnClickListener {
            if(validation()) {
                onDonePressed()
            }
        }
        binding.title.doAfterTextChanged {
            binding.done.show()
            binding.edit.hide()
        }
        binding.description.doAfterTextChanged {
            binding.done.show()
            binding.edit.hide()
        }
    }

    private fun onRemoveImage(pos: Int, item: Uri) {
        adapter.removeItem(pos)
    }

    private fun showAddTagDialog(){
        val dialog = requireContext().createDialog(R.layout.add_tag_dialog, true)
        val button = dialog.findViewById<MaterialButton>(R.id.tag_dialog_add)
        val editText = dialog.findViewById<EditText>(R.id.tag_dialog_et)
        button.setOnClickListener {
            if (editText.text.toString().isNullOrEmpty()) {
                toast(getString(R.string.error_tag_text))
            } else {
                val text = editText.text.toString()
                tagsList.add(text)
                binding.tags.apply {
                    addChip(text, true) {
                        tagsList.forEachIndexed { index, tag ->
                            if (text.equals(tag)) {
                                tagsList.removeAt(index)
                                binding.tags.removeViewAt(index)
                            }
                        }
                        if (tagsList.size == 0){
                            layoutParams.height = 40.dpToPx
                        }
                    }
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                binding.done.show()
                binding.edit.hide()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun addTags(note: MutableList<String>) {
        if (note.size > 0) {
            binding.tags.apply {
                removeAllViews()
                note.forEachIndexed { index, tag ->
                    addChip(tag, true) {
                        if (isEnabled) {
                            note.removeAt(index)
                            this.removeViewAt(index)
                        }
                    }
                }
            }
        }
    }

    private fun isMakeEnableUI(isDisable: Boolean = false) {
        binding.title.isEnabled = isDisable
        binding.date.isEnabled = isDisable
        binding.tags.isEnabled = isDisable
        binding.addTagLl.isEnabled = isDisable
        binding.description.isEnabled = isDisable
    }

    private fun validation(): Boolean {
        var isValid = true
        if (binding.title.text.toString().isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.error_title))
        }
        if (binding.description.text.toString().isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.error_description))
        }
        return isValid
    }

    private fun getNote(): Note {
        return Note(
            id = objNote?.id ?: "",
            title = binding.title.text.toString(),
            description = binding.description.text.toString(),
            tags = tagsList,
            images = getImageUrls(),
            date = Date()
        ).apply {
            authViewModel.gerSession {
                this.user_id = it?.id ?: ""
            }
        }
    }

    private fun getImageUrls(): List<String> {
        if(imageUris.isNotEmpty()) {
            return imageUris.map { it.toString() }
        } else {
            return objNote?.images ?: arrayListOf()
        }
    }

    private fun onDonePressed() {
        if(imageUris.isNotEmpty()) {
            viewModel.uploadSingleFile(imageUris.first()) { state ->
                when(state) {
                    is UiState.Loading -> {
                        binding.progressBar.show()
                    }
                    is UiState.Failure -> {
                        binding.progressBar.hide()
                        toast(state.error)
                    }
                    is UiState.Success -> {
                        binding.progressBar.hide()
                        if (objNote == null) {
                            viewModel.addNote(getNote())
                        } else {
                            viewModel.updateNote(getNote())
                        }
                    }
                }
            }
        } else {
                if (objNote == null) {
                    viewModel.addNote(getNote())
                } else {
                    viewModel.updateNote(getNote())
                }
        }
    }


}