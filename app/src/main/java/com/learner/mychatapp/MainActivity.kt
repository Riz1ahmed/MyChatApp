package com.learner.mychatapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mychatapp.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.learner.codereducer.utils.extentions.toast

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBase()
    }

    private fun fireBase() {
        val database = Firebase.database.getReference("Msg")

        database.setValue("Hello")

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                toast("Data updated")
            }

            override fun onCancelled(error: DatabaseError) {
                //Failed to read
            }
        })
    }
}