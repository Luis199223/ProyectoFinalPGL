package com.example.luisramosmedina.yoursnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {
    //Definimos las variables
    private static final int ADD = Menu.FIRST;
    private static final int DELETE = Menu.FIRST + 1;
    private static final int EXIST = Menu.FIRST + 2;

    ListView lista;
    TextView textLista;
    ConexionBD BD;
    List<String> item = null;
    String getTitulo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Referenciamos los elementos de la interfaz
        textLista = (TextView) findViewById(R.id.textView_Lista);
        lista = (ListView) findViewById(R.id.ListView_Lista);
        //Preparamos la selección del ListView
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getTitulo = (String) lista.getItemAtPosition(position);
                alert("list");
            }
        });

        //Lanzamos la visualización de las notas:
        showNotes();

    }

    //Método que crea el menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        /**Aquí posicionamos cada opción del menú,pasamos el int creado
         * anteriormente y luego abrimos el string creado.
         */
        menu.add(1, ADD, 0, R.string.menu_crear);
        menu.add(2, DELETE, 0, R.string.menu_borrar_todas);
        menu.add(3, EXIST, 0, R.string.menu_salir);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    //Méotdo que captura la opción seleccionada del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Ponemos los casos en función del item seleccionado
        switch (id) {
            case ADD:
                actividad("add");
                return true;
            case DELETE:
                alert("deletes");
                return true;
            case EXIST:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    //Creamos un método para visualizar las notas
    private void showNotes() {
        BD = new ConexionBD(this);
        Cursor c = BD.getNotes();
        item = new ArrayList<String>();
        String title = "";
        //Comprobamos que existe al menos un registro
        if (c.moveToFirst() == false) {
            //El cursor está vacío
            textLista.setText("No hay Notas");
        } else {
            //Recorremos el cursor hasta que no queden recursos
            do {
                title = c.getString(1);

                item.add(title);
            } while (c.moveToNext());
        }
        //Creamos el array que llenará la lista
        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
        lista.setAdapter(adaptador);
    }

    //Creamos un método para recoger las notas
    public String getNote() {
        String type = "", content = "";

        BD = new ConexionBD(this);
        Cursor c = BD.getNote(getTitulo);
        //Comprobamos que existe al menos un registro
        if (c.moveToFirst()) {

            //recorremos el cursor hasta que no queden recursos
            do {
                content = c.getString(2);

            } while (c.moveToNext());
        }
        return content;
    }

    //Método que lanza la actividad de AgregarNota
    public void actividad(String act) {

        String type = "", content = "";
        if (act.equals("add")) {
            type = "add";
            Intent intent = new Intent(Principal.this, AgregarNota.class);
            intent.putExtra("type", type);
            startActivity(intent);

        } else {
            if (act.equals("edit")) {
                type = "edit";
                content = getNote();
                Intent intent = new Intent(Principal.this, AgregarNota.class);
                intent.putExtra("type", type);
                intent.putExtra("title", getTitulo);
                intent.putExtra("content", content);
                startActivity(intent);
            } else {
                if (act.equals("see")) {
                    content = getNote();
                    Intent intent = new Intent(Principal.this, VerNota.class);
                    intent.putExtra("title", getTitulo);
                    intent.putExtra("content", content);
                    startActivity(intent);

                }
            }
        }
    }

    //Creamos un método para mostrar unas alertas al seleccionar las notas
    private void alert(String f) {
        AlertDialog alerta;
        alerta = new AlertDialog.Builder(this).create();
        if (f.equals("list")) {
            alerta.setTitle("El título de la Nota: " + getTitulo);
            alerta.setMessage("¿Qué acción desea realizar?");
            alerta.setButton(DialogInterface.BUTTON_POSITIVE, "Ver Nota", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actividad("see");
                    //Notificamos  que se borra correctamente
                    String msj;
                    msj = "Nota Visualizada";
                    Mensaje(msj);
                }
            });
            alerta.setButton(DialogInterface.BUTTON_NEUTRAL, "Borrar Nota", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delete("delete");
                    Intent intent = getIntent();
                    startActivity(intent);
                    //Notificamos  que se borra correctamente
                    String msj;
                    msj = "Nota borrada";
                    Mensaje(msj);
                }
            });
            alerta.setButton(DialogInterface.BUTTON_NEGATIVE, "Editar Nota", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actividad("edit");
                }
            });
        } else {
            if (f.equals("deletes")) {
                alerta.setTitle("Mensaje de Confirmación");
                alerta.setMessage("¿Qué acción desea realizar?");
                alerta.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alerta.setButton(DialogInterface.BUTTON_POSITIVE, "Borrar Notas", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete("deletes");
                        Intent intent = getIntent();
                        startActivity(intent);
                        //Notificamos  que se borra correctamente
                        String msj;
                        msj = "Notas borradas";
                        Mensaje(msj);

                    }
                });
            }
        }
        alerta.show();
    }

    //Creamos el método para borrar
    private void delete(String f) {
        BD = new ConexionBD(this);
        if (f.equals("delete")) {
            BD.deleteNote(getTitulo);
        } else {
            if (f.equals("deletes")) {
                BD.AllDeleteNotes();
            }
        }

    }
    //Ahora creamos un método que contendrá el mensaje
    public void Mensaje(String msj){
        Toast toast = Toast.makeText(this, msj, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }
}

