package com.example.roomcoroutines.ui.second

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.roomcoroutines.R
import com.example.roomcoroutines.db.Note
import kotlinx.android.synthetic.main.activity_new_word.*

class NewWordActivity : AppCompatActivity() {

    private var selectedNoteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        val selectedNote: Note? = intent.getParcelableExtra("selectedNote")
        selectedNote?.let {
            edit_word.setText(it.title)
            description_edit_text.setText(it.description)
            selectedNoteId = it.id
        }

        button_save.setOnClickListener {
            val intent = Intent()

            if ((edit_word.text).isEmpty() || (description_edit_text.text).isEmpty()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val word = edit_word.text.toString()
                val description = description_edit_text.text.toString()
                val note = Note(title = word, description = description, id = selectedNoteId)
                intent.putExtra(EXTRA_REPLY, note)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.REPLY"
    }
}
