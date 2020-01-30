/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.datasource.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dev.ugscheduler.shared.util.Constants

val FirebaseFirestore.students: CollectionReference get() = collection(Constants.STUDENTS)
val FirebaseFirestore.anonymous: CollectionReference get() = collection(Constants.ANONYMOUS)
val FirebaseFirestore.facilitators: CollectionReference get() = collection(Constants.FACILITATORS)
val FirebaseFirestore.courses: CollectionReference get() = collection(Constants.COURSES)
val FirebaseFirestore.feedback: CollectionReference get() = collection(Constants.FEEDBACK)
val FirebaseFirestore.enrolment: CollectionReference get() = collection(Constants.ENROLMENTS)
val FirebaseFirestore.news: CollectionReference get() = collection(Constants.NEWS)

fun FirebaseFirestore.studentDocument(id: String): DocumentReference =
    collection(Constants.STUDENTS).document(id)

fun FirebaseFirestore.anonymousDocument(id: String): DocumentReference =
    collection(Constants.ANONYMOUS).document(id)

fun FirebaseFirestore.facilitatorDocument(id: String): DocumentReference =
    collection(Constants.FACILITATORS).document(id)

fun FirebaseFirestore.courseDocument(id: String): DocumentReference =
    collection(Constants.COURSES).document(id)

fun FirebaseFirestore.feedbackDocument(id: String): DocumentReference =
    collection(Constants.FEEDBACK).document(id)

fun FirebaseFirestore.newsDocument(id: String): DocumentReference =
    collection(Constants.NEWS).document(id)