package com.example.roomcoroutines.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomcoroutines.R
import com.example.roomcoroutines.db.Note
import com.example.roomcoroutines.recyclerview.ClickInterface
import com.example.roomcoroutines.recyclerview.MyItemTouchHelper
import com.example.roomcoroutines.recyclerview.NoteListAdapter
import com.example.roomcoroutines.ui.second.NewWordActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

private const val newNoteActivityRequestCode = 1
private const val updateNoteActivityRequestCode = 2

class MainActivity : AppCompatActivity(), ClickInterface {

    private lateinit var noteListAdapter: NoteListAdapter

    private val noteViewModel by viewModel<NoteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()

        noteViewModel.allWords.observe(this, Observer {
            it?.let {
                noteListAdapter.setNotes(it)
            }
        })

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)
        }

    }

    private fun setUpRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        noteListAdapter = NoteListAdapter(this, this)
        ItemTouchHelper(MyItemTouchHelper(noteListAdapter)).attachToRecyclerView(recyclerView)
        recyclerView.apply {
            adapter = noteListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK -> insertNote(
                data
            )
            requestCode == updateNoteActivityRequestCode && resultCode == Activity.RESULT_OK -> updateNote(
                data
            )
            else -> showToast()
        }

    }

    private fun updateNote(data: Intent?) {
        val changedWord: Note? = data?.getParcelableExtra(NewWordActivity.EXTRA_REPLY)
        changedWord?.let {
            noteViewModel.update(it)
        }
    }

    private fun insertNote(data: Intent?) {
        val insertedWord: Note? = data?.getParcelableExtra(NewWordActivity.EXTRA_REPLY)
        insertedWord?.let {
            noteViewModel.insert(it)
        }
    }

    private fun showToast() {
        Toast.makeText(
            applicationContext,
            R.string.empty_not_saved,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this, NewWordActivity::class.java)
        intent.putExtra("selectedNote", note).apply {
            startActivityForResult(this, updateNoteActivityRequestCode)
        }
    }

    override fun onSwipe(note: Note) {
        noteViewModel.deleteNote(note)
        Snackbar.make(fab, "item deleted", Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        noteViewModel.clear()
        Snackbar.make(fab, "all item deleted", Snackbar.LENGTH_SHORT)
            .show()
        return super.onOptionsItemSelected(item)
    }
}
