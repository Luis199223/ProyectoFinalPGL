package com.example.luisramosmedina.yoursnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Luis Ramos Medina on 09/03/2017.
 */

public class VerNota extends ActionBarActivity {
    //Creamos las variables para el menú
    private static final int EDITAR = Menu.FIRST;
    private static final int BORRAR = Menu.FIRST + 1;
    private static final int SALIR = Menu.FIRST + 2;

    //Creamos las variables correspondientes
    String title, content;
    TextView TITLE, CONTENT;
    ConexionBD BD;
    String msj;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_nota);

        //Ahora  vamos a capturar los datos de AgregarNota a VerNota
        Bundle bundle = this.getIntent().getExtras();

        title = bundle.getString("title");
        content = bundle.getString("content");

        //Ahora referenciamos los objetos
        TITLE = (TextView) findViewById(R.id.textView_Titulo);
        CONTENT = (TextView) findViewById(R.id.textView_Content);
        TITLE.setText(title);
        CONTENT.setText(content);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //Ahora vamos a crear la opción de añadirle un menú al ver_nota
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);

        //Añadimos las opciones y posiciones al menu:
        menu.add(1, EDITAR, 0, R.string.menu_editar);
        menu.add(2, BORRAR, 0, R.string.menu_eliminar);
        menu.add(3, SALIR, 0, R.string.menu_salir);

        return true;
    }

    /*
   Creamos el método para realizar la acción del menú seleccionado
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Mediante el getItemId se obtiene el valor del botón pulsado.
        switch (id) {
            case EDITAR:
                //Si el botón pulsado es editar editamos la nota
                actividad("edit");
                return true;
            case BORRAR:
                //Si el botón pulsado es borrar borramos la nota
                alert();
                return true;
                //Si el botón pulsado es salir, finalización de la aplicación
            case SALIR:
                actividad("delete");
                return true;
            //Ponemos el break;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //Método que lanza la actividad de AgregarNota cuando la vamos a editar
    public void actividad(String f) {
        if (f.equals("edit")) {
            String type = "edit";
            Intent intent = new Intent(VerNota.this, AgregarNota.class);
            intent.putExtra("type", type);
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            startActivity(intent);
        } else {
            if (f.equals("delete")) {
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                //Llamamos a la actividad
                Intent intent = new Intent(VerNota.this, Principal.class);
                //Con esto borramos la anterior actividad y abrimos la nueva
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    //Creamos un método con mensaje de confirmación
    private void alert() {
        AlertDialog alerta;
        alerta = new AlertDialog.Builder(this).create();
        alerta.setTitle("Mensaje de Confirmación");
        alerta.setMessage("¿Desea eliminar la nota?");
        alerta.setButton(DialogInterface.BUTTON_POSITIVE, "Borrar Nota", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Notificamos que se borra correctamente
                        msj = "Nota Borrada";
                        Mensaje(msj);
                        delete();

                    }
                });

        alerta.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        alerta.show();

    }
    //Creamos un método que borra las notas
    private void delete(){
        BD = new ConexionBD(this);
        BD.deleteNote(title);
        actividad("delete");
    }
    //Ahora creamos un método que contendrá el mensaje
    public void Mensaje(String msj){
        Toast toast = Toast.makeText(this, msj, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }
}
