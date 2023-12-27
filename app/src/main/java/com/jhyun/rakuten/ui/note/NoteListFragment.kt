package com.jhyun.rakuten.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jhyun.rakuten.R
import com.jhyun.rakuten.databinding.FragmentNoteListBinding
import com.jhyun.rakuten.domain.NoteSortType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private val noteListViewModel: NoteListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = noteListViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initNoteAdapter()
        initSortSpinner()

        noteListViewModel.noteSort.observe(viewLifecycleOwner) {
            noteListViewModel.getNotes()
        }

        noteListViewModel.deleteAlertEvent.observe(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext())
                .setMessage(resources.getString(R.string.delete_alert_message, it.size))
                .setPositiveButton(R.string.yes) {_,_-> noteListViewModel.deleteNote(it) }
                .setNegativeButton(R.string.no, null)
                .create()
                .show()
        }

        noteListViewModel.toastEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        noteListViewModel.moveEditorEvent.observe(viewLifecycleOwner) {
            val directions = NoteListFragmentDirections.actionNavNoteListToNavNoteEditor(it)
            findNavController().navigate(directions)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initNoteAdapter() {
        val adapter = NoteAdapter(noteListViewModel)
        binding.noteList.adapter = adapter

        noteListViewModel.noteList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        noteListViewModel.notifyNoteDataEvent.observe(viewLifecycleOwner) {
            val index = adapter.currentList.indexOf(it)
            if(index > -1) {
                adapter.notifyItemChanged(index)
            }
        }
    }

    private fun initSortSpinner() {

        val sortAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.note_sort_spinner,
            android.R.layout.simple_spinner_item
        )
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerSort.adapter = sortAdapter
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val sortType = NoteSortType.values()[position]
                noteListViewModel.setNoteSortType(sortType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

}