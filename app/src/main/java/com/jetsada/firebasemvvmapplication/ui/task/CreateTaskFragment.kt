package com.jetsada.firebasemvvmapplication.ui.task

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsada.firebasemvvmapplication.R
import com.jetsada.firebasemvvmapplication.data.model.Task
import com.jetsada.firebasemvvmapplication.databinding.FragmentCreateTaskBinding
import com.jetsada.firebasemvvmapplication.ui.auth.AuthViewModel
import com.jetsada.firebasemvvmapplication.util.UiState
import com.jetsada.firebasemvvmapplication.util.hide
import com.jetsada.firebasemvvmapplication.util.show
import com.jetsada.firebasemvvmapplication.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class CreateTaskFragment : BottomSheetDialogFragment() {

    val TAG: String = "CreateTaskFragment"
    lateinit var binding: FragmentCreateTaskBinding
    val viewModel: TaskViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()
    private var closeFunction: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancel.setOnClickListener {
            this.dismiss()
        }
        binding.done.setOnClickListener {
            if (validation()) {
                viewModel.addTask(getTask())
            }
        }
        observer()
    }

    private fun getTask(): Task {
        val sdf = SimpleDateFormat("dd MMM yyyy hh:mm a")
        return Task(
            id = "",
            description = binding.taskEt.text.toString(),
            date = sdf.format(Date())
        ).apply { authViewModel.gerSession {
            this.user_id = it?.id ?: ""
        } }
    }

    private fun observer(){
        viewModel.addTask.observe(viewLifecycleOwner) { state ->
            when(state){
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
                    this.dismiss()
                }
            }
        }
    }



    private fun validation(): Boolean {
        var isValid = true
        if (binding.taskEt.text.toString().isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.error_task_detail))
        }
        return isValid
    }

    fun setDismissListener(function: (() -> Unit)?) {
        closeFunction = function
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { setupBottomSheet(it) }
        return dialog
    }

    private fun setupBottomSheet(dialogInterface: DialogInterface) {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFunction?.invoke()
    }


}