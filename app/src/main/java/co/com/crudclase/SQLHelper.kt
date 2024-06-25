package co.com.crudclase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SQLHelper(context: MainActivity) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        // const val, para mayor integridad de la informacion sea mas dificil de alterar
        private const val DB_VERSION = 1
        private const val DB_NAME = "students.db"
        private const val DB_TABLE_PERSONA = "tbl_medicamento"
        private const val ID_PERSONA = "id"
        private const val NOMBRE = "nombre"
        private const val PRECIO = "precio_medicamento"
        private const val DOSIS = "dosis_medicamento"

        private const val DB_TABLE_USUARIO = "tbl_usuario"
        private const val ID_USAURIO = "id"
        private const val NOMBRE_USUARIO =  "usuario"
        private const val CORREO = "correo"
            private const val PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        createTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $DB_TABLE_PERSONA")
        db?.execSQL("DROP TABLE IF EXISTS $DB_TABLE_USUARIO")
        createTables(db)
    }

    private fun createTables(db: SQLiteDatabase?) {
        val createTablePersona = (
                "CREATE TABLE $DB_TABLE_PERSONA(" +
                        "$ID_PERSONA INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "$NOMBRE TEXT," +
                        "$PRECIO TEXT," +
                        "$DOSIS TEXT)"
                )

        val createTablePedido = (
                "CREATE TABLE $DB_TABLE_USUARIO(" +
                        "$ID_USAURIO INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "$NOMBRE_USUARIO TEXT," +
                        "$CORREO TEXT," +
                        "$PASSWORD TEXT)"
                )

        db?.execSQL(createTablePersona)
        db?.execSQL(createTablePedido)
    }

    fun insertMedPer(medModel: MedModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOMBRE, medModel.nombre)
        contentValues.put(PRECIO, medModel.precio)
        contentValues.put(DOSIS, medModel.dosis)

        val success = db.insert(DB_TABLE_PERSONA, null, contentValues)
        db.close()

        return success
    }

    @SuppressLint("Range")

    fun getMedPersona(): ArrayList<MedModel>{
        val medPersona: ArrayList<MedModel> = ArrayList()


        val sql = "SELECT * FROM $DB_TABLE_PERSONA ORDER BY $ID_PERSONA DESC"

        val db = this.readableDatabase

        val cursor : Cursor

        try {
            cursor = db.rawQuery(sql, null)
        }catch (e: Exception) {
            e.printStackTrace()
            return ArrayList()
        }
        var id: Int
        var nombre: String
        var precio: String
        var dosis: String

        if (cursor.moveToFirst()) {

            do {
                id = cursor.getInt(cursor.getColumnIndex(ID_PERSONA))
                nombre = cursor.getString(cursor.getColumnIndex(NOMBRE))
                precio = cursor.getString(cursor.getColumnIndex(PRECIO))
                dosis = cursor.getString(cursor.getColumnIndex(DOSIS))

                //Crea un nuevo objeto EstModel con los datos obtenidos.
                val med = MedModel(id, nombre, precio, dosis)
                //Añade el objeto EstModel a la lista listaEst
                medPersona.add(med)
            } while (cursor.moveToNext())
        }
        return medPersona
    }

    fun actMedicamento(medAct: MedModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(NOMBRE, medAct.nombre)
        contentValues.put(PRECIO, medAct.precio)
        contentValues.put(DOSIS, medAct.dosis)
        val success = db.update(DB_TABLE_PERSONA, contentValues, "$ID_PERSONA=${medAct.id}", null)
        db.close()
        return success
    }

    fun deleteMedicamentoById(id: Int?) {
        val db = writableDatabase
        db.delete(DB_TABLE_PERSONA,"$ID_PERSONA=$id",null)
        db.close()
    }

    fun insertUser(user : MedModelUser): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NOMBRE_USUARIO, user.usuario)
        contentValues.put(CORREO, user.correo)
        contentValues.put(PASSWORD, user.password)

        val success = db.insert(DB_TABLE_USUARIO, null, contentValues)
        db.close()

        return success
    }

    fun Authentification(user: MedModelUser): Boolean {
        val db = this.readableDatabase
        var cursor: Cursor?=null
        var autehnticated = false

        try{
            val query = "SELECT * FROM $DB_TABLE_USUARIO WHERE $NOMBRE_USUARIO = '?' AND $PASSWORD = '?'"
            cursor = db.rawQuery(query, arrayOf(user.usuario, user.password))
            if (cursor.count > 0) {
                autehnticated = true
            }
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            cursor?.close()
            db.close()
        }
        return autehnticated
    }
}