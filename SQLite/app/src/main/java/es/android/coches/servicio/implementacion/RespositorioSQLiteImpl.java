package es.android.coches.servicio.implementacion;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import es.android.coches.entidad.Pregunta;
import es.android.coches.repositorio.interfaz.Repositorio;

class RepositorioSQLiteImpl extends SQLiteOpenHelper implements Repositorio<Pregunta>
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Preguntas.db";

    public RepositorioSQLiteImpl(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public RepositorioSQLiteImpl(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ContratoPregunta.EntradaPregunta.NOMBRE_TABLA +
                " ("
                + ContratoPregunta.EntradaPregunta._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ContratoPregunta.EntradaPregunta.NOMBRE + " TEXT NOT NULL,"
                + ContratoPregunta.EntradaPregunta.FOTO + " TEXT NOT NULL,"
                + "UNIQUE (" + ContratoPregunta.EntradaPregunta.NOMBRE + "))");


        Pregunta pregunta1 = new Pregunta("Seat", "seat.jpeg");
        this.save(pregunta1, db);
        Pregunta pregunta2 = new Pregunta("Dacia", "dacia.jpeg");
        this.save(pregunta2, db);
        Pregunta pregunta3 = new Pregunta("Mercedes", "mercedes.jpeg");
        this.save(pregunta3, db);
        Pregunta pregunta4 = new Pregunta("BMW", "bmw.jpeg");
        this.save(pregunta4, db);
        Pregunta pregunta5 = new Pregunta("Audi", "audi.jpeg");
        this.save(pregunta5, db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }




    @Override
    public Optional<Pregunta> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Pregunta> getAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                ContratoPregunta.EntradaPregunta.NOMBRE_TABLA, // Nombre de la tabla
                null, // Lista de Columnas a consultar
                null, // Columnas para la cláusula WHERE
                null, // Valores a comparar con las columnas del WHERE
                null, // Agrupar con GROUP BY
                null, // Condición HAVING para GROUP BY
                null // Cláusula ORDER BY
        );

        List<Pregunta> preguntas = new LinkedList<>();
        while(c.moveToNext()){
            @SuppressLint("Range")
            String nombre = c.getString(
                    c.getColumnIndex(ContratoPregunta.EntradaPregunta.NOMBRE));
            @SuppressLint("Range")
            String foto = c.getString(
                    c.getColumnIndex(ContratoPregunta.EntradaPregunta.FOTO));
            preguntas.add(new Pregunta(nombre, foto));
        }

        return preguntas;
    }

    @Override
    public void save(Pregunta pregunta) {
        this.save(pregunta, null);
    }
    // creamos nuevo método save() que se usa internamente en el onCreate
    private void save(Pregunta pregunta, SQLiteDatabase db) {
        if(db == null)
            db = getWritableDatabase();
        // Contenedor de valores
        ContentValues values = new ContentValues();
        // Pares clave-valor
        values.put(ContratoPregunta.EntradaPregunta.NOMBRE, pregunta.getNombre());
        values.put(ContratoPregunta.EntradaPregunta.FOTO, pregunta.getFoto());
        // Insertar...
        db.insert(ContratoPregunta.EntradaPregunta.NOMBRE_TABLA, null, values);
    }


    @Override
    public void update(Pregunta pregunta) {
        // Obtenemos la BBDD para escritura
        SQLiteDatabase db = getWritableDatabase();
        // Contenedor de valores
        ContentValues values = new ContentValues();
        // Pares clave-valor
        values.put(ContratoPregunta.EntradaPregunta.NOMBRE, pregunta.getNombre());
        values.put(ContratoPregunta.EntradaPregunta.FOTO, pregunta.getFoto());
        // Actualizar...
        db.update(ContratoPregunta.EntradaPregunta.NOMBRE_TABLA,
                values,
                "nombre=?", // filtro para seleccionar registro a actualizar
                new String[] {pregunta.getNombre()}); // array para sustituir ? por valores
    }

    @Override
    public void delete(Pregunta pregunta) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ContratoPregunta.EntradaPregunta.NOMBRE_TABLA,
                "nombre=?", // filtro para seleccionar registro a borrar
                new String[] {pregunta.getNombre()}); // array para sustituir ? por valores
    }

}

class ContratoPregunta {
    /* contructor privado parar que no se pueda instanciar la clase
   accidentalmente */
    private ContratoPregunta() {}
    public static class EntradaPregunta implements BaseColumns {
        public static final String NOMBRE_TABLA = "Pregunta";
        public static final String NOMBRE = "nombre";
        public static final String FOTO = "foto";
    }

}
