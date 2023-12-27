package com.jhyun.rakuten.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.jhyun.rakuten.domain.NoteSortType
import com.jhyun.rakuten.domain.entity.NoteEntity
import com.jhyun.rakuten.domain.usecase.GetNotesUseCase
import com.jhyun.rakuten.domain.usecase.RemoveNotesUseCase
import com.jhyun.rakuten.ui.BaseNoteViewModel
import com.jhyun.rakuten.ui.note.editor.NoteEditorAction
import com.jhyun.rakuten.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val removeNotesUseCase: RemoveNotesUseCase,
) : BaseNoteViewModel() {

    private val _deleteButtonEnable = MutableLiveData<Boolean>(true)
    val deleteButtonEnable: LiveData<Boolean> = _deleteButtonEnable

    private val _noteSort = MutableLiveData<NoteSortType>()
    val noteSort: LiveData<NoteSortType> = _noteSort

    private val _noteList = MutableLiveData<List<NoteListData>>()
    val noteList: LiveData<List<NoteListData>> = _noteList

    private val _noteListMode = MutableLiveData<NoteListMode>(NoteListMode.DEFAULT)
    val noteListMode: LiveData<NoteListMode> = _noteListMode

    val headerVisible: LiveData<Boolean> = _noteList.map { it != null && it.isNotEmpty() }

    private val _loadingEvent = SingleLiveEvent<Boolean>(false)
    val loadingEvent: LiveData<Boolean> = _loadingEvent

    private val _notifyNoteDataEvent = SingleLiveEvent<NoteListData>()
    val notifyNoteDataEvent: LiveData<NoteListData> = _notifyNoteDataEvent

    private val _deleteAlertEvent = SingleLiveEvent<List<NoteEntity>>()
    val deleteAlertEvent: LiveData<List<NoteEntity>> = _deleteAlertEvent

    private val _moveEditorEvent = SingleLiveEvent<NoteEditorAction>()
    val moveEditorEvent: LiveData<NoteEditorAction> = _moveEditorEvent

    override fun onCleared() {
        super.onCleared()
        setLoading(false)
    }

    fun setNoteSortType(type: NoteSortType) {
        if (_noteSort.value == type) return
        _noteSort.value = type
    }

    fun getNotes() {
        setNoteListMode(NoteListMode.DEFAULT)

        noteSort.value?.let { noteSortType ->
            setLoading(true)

            viewModelScope.launch(Dispatchers.IO) {
                val list = getNotesUseCase(noteSortType)
                    .map { it.toData() }
                _noteList.postValue(list)
            }.apply {
                invokeOnCompletion { setLoading(false) }
            }

        }
    }


    fun clickAddNote() {
        _moveEditorEvent.value = NoteEditorAction.New(Date())
    }

    fun clickNoteItem(data: NoteListData) {
        noteListMode.value?.let {
            when (it) {
                NoteListMode.DEFAULT -> {
                    _moveEditorEvent.value = NoteEditorAction.Edit(data.id)
                }

                NoteListMode.SELECT -> {
                    data.reverseSelected()

                    _notifyNoteDataEvent.value = data
                }
            }
        }
    }

    fun clickLongNoteItem(noteData: NoteListData): Boolean {
        setNoteListMode(NoteListMode.SELECT)
        return false
    }

    fun clickSelectAllNote() {
        _noteList.value = _noteList.value?.toMutableList()?.map { it.setSelected(true) }
    }

    fun clickSelectCancel() {
        _noteList.value = _noteList.value?.toMutableList()?.map { it.setSelected(false) }
        setNoteListMode(NoteListMode.DEFAULT)
    }

    fun clickDeleteNote() {
        val removeNoteList = _noteList.value?.filter { it.selected }?.map { it.toEntity() }

        if (removeNoteList.isNullOrEmpty()) {
            sendToastMessage("Select Delete Note Item")
        } else {
            _deleteAlertEvent.value = removeNoteList!!
        }
    }

    fun deleteNote(removeNoteList: List<NoteEntity>) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            removeNotesUseCase(removeNoteList)
            withContext(Dispatchers.Main) { getNotes() }
        }.apply {
            invokeOnCompletion { setLoading(false) }
        }
    }

    private fun setNoteListMode(mode: NoteListMode) {
        _noteListMode.value = mode
    }


    private fun setLoading(isLoading: Boolean) {
        viewModelScope.launch { _loadingEvent.value = isLoading }
    }


}