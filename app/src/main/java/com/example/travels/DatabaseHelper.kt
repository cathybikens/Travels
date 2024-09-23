package com.example.travels

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = """
            CREATE TABLE user (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                phone TEXT NOT NULL,
                role TEXT NOT NULL
            )
        """.trimIndent()

        val createVehicleTable = """
            CREATE TABLE vehicle (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                driver_id INTEGER NOT NULL,
                total_seats INTEGER NOT NULL,
                FOREIGN KEY(driver_id) REFERENCES user(id)
            )
        """.trimIndent()

        val createBookingTable = """
            CREATE TABLE booking (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                vehicle_id INTEGER NOT NULL,
                customer_id INTEGER NOT NULL,
                destination TEXT NOT NULL,
                seats_booked INTEGER NOT NULL,
                FOREIGN KEY(vehicle_id) REFERENCES vehicle(id),
                FOREIGN KEY(customer_id) REFERENCES user(id)
            )
        """.trimIndent()

        val createDestinationTable = """
            CREATE TABLE destination (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                price REAL NOT NULL
            )
        """.trimIndent()

        db.execSQL(createUserTable)
        db.execSQL(createVehicleTable)
        db.execSQL(createBookingTable)
        db.execSQL(createDestinationTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user")
        db.execSQL("DROP TABLE IF EXISTS vehicle")
        db.execSQL("DROP TABLE IF EXISTS booking")
        db.execSQL("DROP TABLE IF EXISTS destination")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "travels.db"
        private const val DATABASE_VERSION = 1
    }
}
