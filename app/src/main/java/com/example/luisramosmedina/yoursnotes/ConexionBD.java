package com.example.luisramosmedina.yoursnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Luis Ramos Medina on 09/03/2017.
 */

/*Creamos la base de datos donde se guardarán
los datos de las notas guardadas.
 */


public class ConexionBD extends SQLiteOpenHelper{
    //Creamos las variables correspondientes
    //Strings finales que contienen las cadenas de lo que se guardará en la BD
    public static final String TABLE_ID ="_idNote";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    //Nombre de la base de datos, de la tabla de la base de datos a utilizar.
    private static  final String DATABASE = "Note";
    private  static  final String TABLE = "notes";

    public ConexionBD(Context context) {
        super(context, DATABASE, null, 1);
    }
    /*Ete método se encarga de inicializar la base de datos
    se ejecuta siempre cuando se crea la clase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creamos la tabla en la BD
        db.execSQL("CREATE TABLE "+ TABLE +" ("+ TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + TITLE + " TEXT,"+ CONTENT +" TEXT)");


    }
    /*Método usado en el caso de que haga falta actualizar
     la versión de la base de datos
      */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE);
        onCreate(db);
    }
    //Creamos el método que añadirá las notas
    public void  addNote (String title,String content){
        ContentValues valores = new ContentValues();
        valores.put(TITLE, title);
        valores.put(CONTENT, content);
        this.getWritableDatabase().insert(TABLE,null,valores);
    }
    //Creamos un método  consultará las notas
    public Cursor getNote(String condition){
        String colummnas[]={TABLE_ID,TITLE,CONTENT};
        String[] args = new String[] {condition};
        Cursor c = this.getReadableDatabase().query(TABLE, colummnas, TITLE+"=?",args,null,null,null);
        return c;
    }
    //Creamos un método para borrar nota
    public  void deleteNote(String condition){
        String args[]={condition};
        this.getWritableDatabase().delete(TABLE,TITLE +"=?",args);
    }
    //Creamos un método para editar nota
    public  void updateNote(String title, String content, String condition){
        String args[]={condition};
        ContentValues valores = new ContentValues();
        valores.put(TITLE, title);
        valores.put(CONTENT, content);
        this.getWritableDatabase().update(TABLE,valores,TITLE +"=?",args);
    }
    //Creamos un método para coger las notas
    public Cursor getNotes() {
        String columnas[]={TABLE_ID,TITLE,CONTENT};
        Cursor c = this.getReadableDatabase().query(TABLE, columnas,null,null,null,null,null);
        return c;
    }
    //Creamos un método para borrar_todas las notas
    public void  AllDeleteNotes(){
        this.getWritableDatabase().delete(TABLE,null,null);
    }


}
