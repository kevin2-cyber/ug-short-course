package dev.codelabs.firebasetestapp

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*

val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)

fun debugger(msg: Any?) = println("FirebaseTestApp =>  ${msg.toString()}")

fun RecyclerView.setupWithAdapter(adapter: ListAdapter<DummyData, DataViewHolder>) {
    this.adapter = adapter
    this.layoutManager = LinearLayoutManager(this.context)
    this.itemAnimator = DefaultItemAnimator()
    this.setHasFixedSize(false)
}

fun FirebaseFirestore.push(data: DummyData) =
    collection("test-data").document(data.id).set(
        data,
        SetOptions.merge()
    ).addOnCompleteListener { }.addOnFailureListener { }

fun FirebaseDatabase.push(data: DummyData) =
    reference.child("test-data").child(data.id).setValue(data)
        .addOnFailureListener { }
        .addOnCompleteListener { }

const val USER_REF = "users"
const val CHANNEL_ID = "firebase test app"

// App user's data model
data class AppUser constructor(
    val id: String,
    val email: String?,
    val timestamp: Long = System.currentTimeMillis() //e.g. 157029309343
) {
    constructor() : this("", "")
}

data class DummyData(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "Lorem ipsum dolor.",
    val description: String = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32."
) {
    constructor() : this("", "", "")
}

