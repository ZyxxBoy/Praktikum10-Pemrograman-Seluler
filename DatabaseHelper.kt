package com.example.donutapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DonutApp.db"
        private const val DATABASE_VERSION = 1

        // Table Account
        private const val TABLE_ACCOUNT = "account"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PASSWORD = "password"

        // Table Donut
        private const val TABLE_DONUT = "donut"
        private const val COLUMN_ID_DONUT = "idDonut"
        private const val COLUMN_NAME_DONUT = "name"
        private const val COLUMN_PRICE_DONUT = "price"
        private const val COLUMN_DESC_DONUT = "description"
        private const val COLUMN_IMAGE_DONUT = "image"

        // Table Transaction
        private const val TABLE_TRANS = "transaksi"
        private const val COLUMN_ID_TRANS = "idTransaksi"
        private const val COLUMN_TGL = "tanggal"
        private const val COLUMN_USER = "user"

        // Table Detail Transaction
        private const val TABLE_DET_TRANS = "detailTrans"
        private const val COLUMN_ID_DET_TRX = "idDetailTrx"
        private const val COLUMN_ID_TRX = "idTransaksi"
        private const val COLUMN_ID_DONUT_TRX = "idDonut"
        private const val COLUMN_HARGA = "harga"
        private const val COLUMN_JUMLAH = "jumlah"
    }

    // Create tables
    private val CREATE_ACCOUNT_TABLE = ("CREATE TABLE $TABLE_ACCOUNT ("
            + "$COLUMN_EMAIL TEXT PRIMARY KEY, "
            + "$COLUMN_NAME TEXT, "
            + "$COLUMN_PASSWORD TEXT)")

    private val CREATE_DONUT_TABLE = ("CREATE TABLE $TABLE_DONUT ("
            + "$COLUMN_ID_DONUT INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "$COLUMN_NAME_DONUT TEXT, "
            + "$COLUMN_PRICE_DONUT INTEGER, "
            + "$COLUMN_DESC_DONUT TEXT, "
            + "$COLUMN_IMAGE_DONUT TEXT)")

    private val CREATE_TRANSACTION_TABLE = ("CREATE TABLE $TABLE_TRANS ("
            + "$COLUMN_ID_TRANS INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "$COLUMN_TGL TEXT, "
            + "$COLUMN_USER TEXT)")

    private val CREATE_DET_TRANS_TABLE = ("CREATE TABLE $TABLE_DET_TRANS ("
            + "$COLUMN_ID_DET_TRX INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "$COLUMN_ID_TRX INTEGER, "
            + "$COLUMN_ID_DONUT_TRX INTEGER, "
            + "$COLUMN_HARGA INTEGER, "
            + "$COLUMN_JUMLAH INTEGER)")

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_ACCOUNT_TABLE)
        db.execSQL(CREATE_DONUT_TABLE)
        db.execSQL(CREATE_TRANSACTION_TABLE)
        db.execSQL(CREATE_DET_TRANS_TABLE)
        insertInitialDonuts(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DONUT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DET_TRANS")
        onCreate(db)
    }

    private fun insertInitialDonuts(db: SQLiteDatabase) {
        val donuts = listOf(
            Donut(1, "Glazed Donut", 15000, "Classic glazed donut", "donut1"),
            Donut(2, "Chocolate Donut", 18000, "Chocolate frosted donut", "donut2"),
            Donut(3, "Strawberry Donut", 20000, "Strawberry frosted donut", "donut3"),
            Donut(4, "Boston Cream", 22000, "Cream filled donut", "donut4"),
            Donut(5, "Jelly Donut", 19000, "Jelly filled donut", "donut5")
        )

        for (donut in donuts) {
            val values = ContentValues().apply {
                put(COLUMN_ID_DONUT, donut.id)
                put(COLUMN_NAME_DONUT, donut.name)
                put(COLUMN_PRICE_DONUT, donut.price)
                put(COLUMN_DESC_DONUT, donut.description)
                put(COLUMN_IMAGE_DONUT, donut.image)
            }
            db.insert(TABLE_DONUT, null, values)
        }
    }

    // Account functions
    fun addAccount(email: String, name: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_NAME, name)
            put(COLUMN_PASSWORD, password)
        }
        val result = db.insert(TABLE_ACCOUNT, null, values)
        return result != -1L
    }

    fun checkAccount(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_ACCOUNT WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        return cursor.count > 0
    }

    // Donut functions
    fun getAllDonuts(): List<Donut> {
        val donuts = mutableListOf<Donut>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_DONUT", null)

        if (cursor.moveToFirst()) {
            do {
                val donut = Donut(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
                donuts.add(donut)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return donuts
    }

    // Transaction functions
    fun addTransaction(email: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val tanggal = sdf.format(Date())
            put(COLUMN_TGL, tanggal)
            put(COLUMN_USER, email)
        }
        return db.insert(TABLE_TRANS, null, values)
    }

    fun addDetailTransaction(idTransaksi: Long, idDonut: Int, harga: Int, jumlah: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID_TRX, idTransaksi)
            put(COLUMN_ID_DONUT_TRX, idDonut)
            put(COLUMN_HARGA, harga)
            put(COLUMN_JUMLAH, jumlah)
        }
        val result = db.insert(TABLE_DET_TRANS, null, values)
        return result != -1L
    }

    // CRUD for Donut
    fun addDonut(donut: Donut): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME_DONUT, donut.name)
            put(COLUMN_PRICE_DONUT, donut.price)
            put(COLUMN_DESC_DONUT, donut.description)
            put(COLUMN_IMAGE_DONUT, donut.image)
        }
        val result = db.insert(TABLE_DONUT, null, values)
        return result != -1L
    }

    fun updateDonut(donut: Donut): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME_DONUT, donut.name)
            put(COLUMN_PRICE_DONUT, donut.price)
            put(COLUMN_DESC_DONUT, donut.description)
            put(COLUMN_IMAGE_DONUT, donut.image)
        }
        val result = db.update(
            TABLE_DONUT,
            values,
            "$COLUMN_ID_DONUT = ?",
            arrayOf(donut.id.toString())
        )
        return result > 0
    }

    fun deleteDonut(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(
            TABLE_DONUT,
            "$COLUMN_ID_DONUT = ?",
            arrayOf(id.toString())
        )
        return result > 0
    }

    fun getDonutById(id: Int): Donut? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_DONUT,
            arrayOf(
                COLUMN_ID_DONUT,
                COLUMN_NAME_DONUT,
                COLUMN_PRICE_DONUT,
                COLUMN_DESC_DONUT,
                COLUMN_IMAGE_DONUT
            ),
            "$COLUMN_ID_DONUT = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val donut = Donut(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getString(3),
                cursor.getString(4)
            )
            cursor.close()
            donut
        } else {
            cursor.close()
            null
        }
    }
}

data class Donut(
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    val image: String
)
