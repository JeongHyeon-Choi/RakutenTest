package com.jhyun.rakuten.ui.note.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jhyun.rakuten.R
import com.jhyun.rakuten.databinding.FragmentNoteEditorBinding
import com.jhyun.rakuten.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteEditorFragment : Fragment() {


    private var _binding: FragmentNoteEditorBinding? = null
    private val binding get() = _binding!!

    private val noteEditorViewModel: NoteEditorViewModel by viewModels()

    private val args: NoteEditorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = noteEditorViewModel

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        noteEditorViewModel.setNoteEditorAction(args.action)

        noteEditorViewModel.noteEditing.observe(viewLifecycleOwner) {
            if (it) {
                lifecycleScope.launch {
                    delay(100)
                    Utils.showSoftInputKeyboard(binding.editorTitle, selectionEnd = true)
                }
            } else {
               Utils.hideSoftInputKeyboard(requireActivity())
            }
        }

        noteEditorViewModel.toastEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        noteEditorViewModel.moveBackEvent.observe(viewLifecycleOwner) {
            popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun popBackStack() {
        if (noteEditorViewModel.isEdited.value == true) {
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.saved_alert_message)
                .setPositiveButton(R.string.yes) { _, _ -> findNavController().popBackStack() }
                .setNegativeButton(R.string.no, null)
                .create()
                .show()
        } else {
            findNavController().popBackStack()
        }
    }


}