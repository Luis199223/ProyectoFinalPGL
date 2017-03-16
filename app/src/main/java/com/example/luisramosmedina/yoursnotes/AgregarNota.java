package com.example.luisramosmedina.yoursnotes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Luis Ramos Medina on 09/03/2017.
 */

public class AgregarNota extends ActionBarActivity {

    //Creamos las variables
    String type,getTitle;
    Button Add;

    //Creamos los objetos para recoger los datos a añadir
    EditText TITLE,CONTENT;
    private static  final int SALIR = Menu.FIRST;
    ConexionBD BD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_nota);

        //Colocamos el objeto que referencia el botón agregar del menú
        Add = (Button)findViewById(R.id.button_Add);

        //Referenciamos los objetos
        Add = (Button)findViewById(R.id.button_Add);
        TITLE = (EditText)findViewById(R.id.editText_Titulo);
        CONTENT = (EditText)findViewById(R.id.editText_Nota);

        //Creamos un objeto de la clase type
        Bundle bundle = this.getIntent().getExtras();


        String content;
        getTitle = bundle.getString("title");
        content =  bundle.getString("content");
        type = bundle.getString("type");

        //Capturamos la variables que queremos capturar
        type = bundle.getString("type");

        //Creamos una condición donde evaluamos la variable type
        if (type.equals("add")){
            Add.setText("Añadir Nota");
        }else{
            if (type.equals("edit")){
                TITLE.setText(getTitle);
                CONTENT.setText(content);
                Add.setText("Actualizar Nota");
            }
        }
        //Ejecutamos el método addUpadteNotes para añadir o editar las notas
        Add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               addUpdateNotes();
            }
        });

    }
    //Ahora vamos a crear la opción de añadirle un menú al agregar_nota
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);

        //Añadimos la posición del menú:
        menu.add(1,SALIR,0,R.string.menu_salir);

        return true;
    }
    /*
    Creamos el método para realizar la acción del menú seleccionado
     */
    public  boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //Mediante el getItemId se obtiene el valor del botón pulsado.
        switch (id){
            //Si el botón pulsado es salir, finalización de la aplicación
            case SALIR:
                /*El CookieSyncManager se utiliza para
                 el almacenamiento de cookies en memoria ram y permaneten, es
                 es una forma de optimzar el rendimiento de la APP.
                  */
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                //Llamamos a la actividad
                Intent intent = new Intent(AgregarNota.this, Principal.class);
                //Con esto borramos la anterior actividad y abrimos la nueva
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return  true;
            //Ponemos el break;
            default:
                return  super.onOptionsItemSelected(item);
        }

    }
    //Creamos el método  nos permitirá añadir notas y actualizar las notas
    private  void addUpdateNotes() {
        BD = new ConexionBD(this);
        String title, content, msj;
        title = TITLE.getText().toString();
        content = CONTENT.getText().toString();

        //Realicamos mediante condiciones un control de los datos
        if (type.equals("add")) {
            //Comprobamos que se introduce un título
            if (title.equals("")) {
                msj = "Introduzca un Título";
                TITLE.requestFocus();
                Mensaje(msj);

            } else {
                //Comprobamos que se introduce un contenido
                if (content.equals("")) {
                    msj = "Introduzca el contenido de la nota";
                    CONTENT.requestFocus();
                    Mensaje(msj);
                } else {
                    Cursor c = BD.getNote(title);
                    String gettitle = "";
                    //Ahora nos aseguramos que existe al menos un registro
                    if (c.moveToFirst()) {
                        //Ahora recorremos el cursor hasta que no haya más registros
                        do {
                            //Empezamos desde el valor 1 ya que el valor 0 es el id de Título
                            gettitle = c.getString(1);
                        } while (c.moveToNext());
                    }
                    //Comprobamos que el título introducido no se repite
                    if (gettitle.equals(title)) {
                        TITLE.requestFocus();
                        msj = "El Título de la nota ya existe";
                        Mensaje(msj);
                        //Si esta condición no se cumple se crea la nota
                    } else {
                        BD.addNote(title, content);
                        actividad(title, content);
                        //Notificamos  que se añade correctamente
                        msj = "Nota añadida";
                        Mensaje(msj);
                    }

                }

            }
        }else {
            //Comprobamos que  el contenido es para editar
           if (type.equals("edit")){
                Add.setText("Actualizar Nota");
               //Comprobamos que se introduce un título
               if (title.equals("")){
                   msj = "Introduzca un Título";
                   TITLE.requestFocus();
                   Mensaje(msj);
               }else {
                   //Comprobamos que se introduce un contenido
                   if (content.equals("")){
                       msj = "Introduzca el contenido de la nota";
                       CONTENT.requestFocus();
                       Mensaje(msj);
                   }else{
                       /*
                       Le pasamos los parámetros para actualizar la nota
                       en el método updateNote
                        */
                       BD.updateNote(title,content,getTitle);
                       //Lanzamos el método actividad VerNota
                       actividad(title,content);
                       //Notificamos  que se edita correctamente
                       msj = "Nota editada";
                       Mensaje(msj);
                   }
               }
           }
        }
    }
    //Ahora creamos un método que contendrá el mensaje
    public void Mensaje(String msj){
        Toast toast = Toast.makeText(this, msj, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    //Método que lanza la actividad de VerNota
    public  void actividad(String title,String content){
        Intent intent = new Intent(AgregarNota.this, VerNota.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        startActivity(intent);
    }


}
