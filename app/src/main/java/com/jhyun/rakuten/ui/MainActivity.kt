package com.jhyun.rakuten.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jhyun.rakuten.data.local.NoteDB
import com.jhyun.rakuten.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var noteDB: NoteDB

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onDestroy() {
        super.onDestroy()

        noteDB.close()
    }
}