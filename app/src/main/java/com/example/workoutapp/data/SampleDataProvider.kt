package com.example.workoutapp.data

import java.util.*

class SampleDataProvider {
    companion object{
        private val sampleText1=" Title:\n Date: \n Place:"
        private val sampleText2=" Title:\n" + " Date: \n" + " Place:"
        private val sampleText3=" Title:\n" + " Date: \n" + " Place:"
    private fun getDate(diff:Long): Date {
        return Date(Date().time + diff)
    }
    fun getNotes() = arrayListOf(
        NoteEntity(getDate(0), sampleText1),
        NoteEntity(getDate(1), sampleText2),
        NoteEntity(getDate(2), sampleText3)
        )
    }
}