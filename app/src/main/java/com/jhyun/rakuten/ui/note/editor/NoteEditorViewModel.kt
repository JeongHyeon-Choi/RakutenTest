package com.jhyun.rakuten.ui.note.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jhyun.rakuten.domain.entity.NoteEntity
import com.jhyun.rakuten.domain.usecase.CreateNoteUseCase
import com.jhyun.rakuten.domain.usecase.GetNoteUseCase
import com.jhyun.rakuten.domain.usecase.UpdateNoteUseCase
import com.jhyun.rakuten.ui.BaseNoteViewModel
import com.jhyun.rakuten.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val getNoteUseCase: GetNoteUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
) : BaseNoteViewModel() {

    val title = MutableLiveData<String>("")
    val body = MutableLiveData<String>("")

    private val _originNote = MutableLiveData<NoteEntity>()
    val isEdited: LiveData<Boolean> = title.asFlow().combine(body.asFlow()) { t, b ->
        return@combine !(t == (_originNote.value?.title ?: "") && b == (_originNote.value?.body ?: ""))
    }.asLiveData()

    private val _noteEditing = MutableLiveData<Boolean>(false)
    val noteEditing: LiveData<Boolean> = _noteEditing

    private val _moveBackEvent = SingleLiveEvent<Unit>()
    val moveBackEvent: LiveData<Unit> = _moveBackEvent

    private val _noteEditorAction = MutableLiveData<NoteEditorAction>()
    val noteEditorAction: LiveData<NoteEditorAction> = _noteEditorAction


    fun setNoteEditorAction(action: NoteEditorAction) {
        _noteEditorAction.value = action

        if (action is NoteEditorAction.Edit) {
            setOriginData(action.id)
        }
    }

    private fun setOriginData(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val origin = getNoteUseCase(id)
            _originNote.postValue(origin)
            title.postValue(origin.title)
            body.postValue(origin.body)

        }
    }

    private fun createNote(title: String, body: String, createdAt: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = createNoteUseCase(title, body, createdAt)

            setOriginData(id)
            _noteEditorAction.postValue(NoteEditorAction.Edit(id))
        }
    }

    private fun updateNote(title: String, body: String, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            updateNoteUseCase(id, title, body)

            setOriginData(id)
        }
    }

    fun clickBackButton() {
        _moveBackEvent.call()
    }

    fun clickEditButton() {
        _noteEditing.value = true
    }

    fun clickEditCancelButton() {
        title.value = _originNote.value?.title ?: ""
        body.value = _originNote.value?.body ?: ""
        _noteEditing.value = false
    }

    fun clickSaveButton() {
        val saveTitle = title.value
        val saveBody = body.value

        if (saveTitle.isNullOrBlank()) {
            sendToastMessage("Title is empty.")
        } else if (saveBody.isNullOrBlank()) {
            sendToastMessage("Body is empty.")
        } else {
            _noteEditorAction.value?.let { data ->
                when (data) {
                    is NoteEditorAction.New -> {
                        createNote(saveTitle, saveBody, data.createdAt)
                    }

                    is NoteEditorAction.Edit -> {
                        updateNote(saveTitle, saveBody, data.id)
                    }
                }
            }

            _noteEditing.value = false
        }

    }
}