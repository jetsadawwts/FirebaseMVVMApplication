package com.jetsada.firebasemvvmapplication.code

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jetsada.firebasemvvmapplication.R

class CodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)

//        testAddDataFireStorage()
    }



    private fun testAddDataFireStorage() {
        val db = Firebase.firestore

        /* Add Firebase hashMapOf */
        // TODO: Add Firebase hashMapOf

        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
        )

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

        /* Add Firebase data class */
        // TODO: Add Firebase data class
        // Create a new user with a first, middle, and last name
        val testData = testData3("1","Jetsadawwts")

        // Add a new document ArrayData with a generated ID
        val document =  db.collection("user").document()
        testData.id = document.id
        val handle = document.set(testData)
        handle.addOnSuccessListener { documentReference ->
            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${document.id}")
        }
        handle.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error adding document", e)
        }

        /* Add Firebase Mutibledata */
        // TODO: Add Firebase Mutibledata

//        // Create a new user with a first, middle, and last name
//        val testArrayDate = mutableListOf<testDate>()
//        val testDate1 = testDate("27","29", "1996")
//        testArrayDate.add(testDate1)
//        val testDate2 = testDate("28","28", "1998")
//        testArrayDate.add(testDate2)
//
//        val testArrayData = mutableListOf<testData1>()
//        val testData1 = testData1("1","Jetsadawwts", testArrayDate)
//        testArrayData.add(testData1)
//        val testData2 = testData1("2","Jetsadawwts2", testArrayDate)
//        testArrayData.add(testData2)
//
//        // Add Mutibledata a new document with a generated ID
//        val document =  db.collection("user")
    //        testArrayData.forEach {
//            val handle = document.add(it)
//            handle.addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            handle.addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }
//        }

        /* Add Firebase Mutibledata */
        // TODO: Add Firebase Array Data

//        // Create a new user with a first, middle, and last name
//        val testArrayData = mutableListOf<testDate>()
//        val testDate1 = testDate("27","29", "1996")
//        testArrayData.add(testDate1)
//        val testDate2 = testDate("28","28", "1996")
//        testArrayData.add(testDate2)
//        val testData = testData1("1","Jetsadawwts", testArrayData)
//
//        // Add a new document Array Data with a generated ID
//        val document =  db.collection("user").document()
//        testData.id = document.id
//        val handle = document.set(testData)
//        handle.addOnSuccessListener { documentReference ->
//            Log.d(TAG, "DocumentSnapshot added with ID: ${document.id}")
//        }
//        handle.addOnFailureListener { e ->
//            Log.w(TAG, "Error adding document", e)
//        }

        /* Add Firebase Mutibledata */
        // TODO: Add Firebase Mutible String

//        // Create a new user with a first, middle, and last name
//        val testArrayData = mutableListOf<String>()
//
//        val testDate = testDate("27","29", "1996")
//        val testData = testData4("1","Jetsadawwts", testDate)
//
//        // Add a new document muti string with a generated ID
//        val document =  db.collection("user").document()
//        testData.id = document.id
//        val handle = document.set(testData)
//        handle.addOnSuccessListener { documentReference ->
//            Log.d(TAG, "DocumentSnapshot added with ID: ${document.id}")
//        }
//        handle.addOnFailureListener { e ->
//            Log.w(TAG, "Error adding document", e)
//        }

        /* Add Firebase Mutibledata */
        // TODO: Add Firebase Mutible Array
//        // Create a new user with a first, middle, and last name
//        val testArrayData = mutableListOf<String>()
//        val date = arrayOf<String>("27", "09", "1996")
//        testArrayData.addAll(date)
//        val testData = testData2("1","Jetsadawwts", testArrayData)
//
//        // Add a new document Array Data with a generated ID
//        val document =  db.collection("user").document()
//        testData.id = document.id
//        val handle = document.set(testData)
//        handle.addOnSuccessListener { documentReference ->
//            Log.d(TAG, "DocumentSnapshot added with ID: ${document.id}")
//        }
//        handle.addOnFailureListener { e ->
//            Log.w(TAG, "Error adding document", e)
//        }

    }

    data class testData1 (
        var id: String,
        val name: String,
        val date: List<testDate>
    )

    data class testData2(
        var id: String,
        val name: String,
        val date: MutableList<String>
    )

    data class testData3(
        var id: String,
        val name: String
    )

    data class testData4(
        var id: String,
        val name: String,
        val date: testDate
    )

    data class testDate (
        val day: String,
        val mouth: String,
        val year: String
    )
}