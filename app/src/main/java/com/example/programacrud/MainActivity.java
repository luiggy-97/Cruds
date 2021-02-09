package com.example.programacrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ListView;

import com.example.programacrud.model.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private List<Persona> listaperosna = new ArrayList<Persona>();
   ArrayAdapter<Persona>arrayAdapter;
    EditText nomP,apeP,correoP,passowrdP;
    ListView lisV_persona;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference DatabaseReference;

    Persona selecionPersona;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomP= findViewById(R.id.edit_nombre);
        apeP = findViewById(R.id.edit_Apellido);
        correoP= findViewById(R.id.edit_Correo);
        passowrdP= findViewById(R.id.edit_Contrasena);

        lisV_persona= findViewById(R.id.lv_datosPersonas);
        inicialFirebase();
        listaDatos();

        lisV_persona.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             selecionPersona=(Persona) parent.getItemAtPosition(position);
             nomP.setText(selecionPersona.getNombre());
             apeP.setText(selecionPersona.getApellido());
             correoP.setText(selecionPersona.getEmail());
             passowrdP.setText(selecionPersona.getContraseña());
            }
        });

    }

    private void listaDatos() {
        DatabaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaperosna.clear();
                for(DataSnapshot objSnaptshot: snapshot.getChildren()){
                    Persona p = objSnaptshot.getValue(Persona.class);
                    listaperosna.add(p);

                    arrayAdapter = new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, listaperosna );
                    lisV_persona.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicialFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase =firebaseDatabase.getInstance();
      //  firebaseDatabase.setPersistenceEnabled(true); // persistencia
        DatabaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        String nombre= nomP.getText().toString();
        String apellido = apeP.getText().toString();
        String correo = correoP.getText().toString();
        String passowrd = passowrdP.getText().toString();
        switch (item.getItemId()){
            case R.id.icon_add: {
                if(nombre.equals("")||apellido.equals("")||correo.equals("")||passowrd.equals("")){
                    validacion();
                }else {
                    Persona p= new Persona();
                    p.setId(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(apellido);
                    p.setEmail(correo);
                    p.setContraseña(passowrd);
                    DatabaseReference.child("Persona").child(p.getId()).setValue(p);
                    Toast.makeText(this, "agregado", Toast.LENGTH_SHORT).show();
                    limpiar();


                }
                break;
                }
            case R.id.icon_save: {
                Persona p = new Persona();
                p.setId(selecionPersona.getId());
                p.setNombre(nomP.getText().toString().trim());
                p.setApellido(apeP.getText().toString().trim());
                p.setEmail(correoP.getText().toString().trim());
                p.setContraseña(passowrdP.getText().toString().trim());
                DatabaseReference.child("Persona").child(p.getId()).setValue(p);
                Toast.makeText(this, "guardado", Toast.LENGTH_SHORT).show();
                limpiar();
                break;
            }
            case R.id.icon_delete: {
                Persona p = new Persona();
                p.setId(selecionPersona.getId());
                DatabaseReference.child("Persona").child(p.getId()).removeValue();


                Toast.makeText(this, "eliminado", Toast.LENGTH_SHORT).show();
                break;
            }
            default:break;
        }
        return true;
    }

    private void limpiar() {
        nomP.setText("");
      apeP.setText("");
      correoP.setText("");
         passowrdP.setText("");
    }

    private void validacion() {
        String nombre = nomP.getText().toString() ;
        String apellido = apeP.getText().toString();
        String correo = correoP.getText().toString();
        String passowrd = passowrdP.getText().toString();
        if (nombre.equals("")){
            nomP.setError("Required");
        }else if(apellido.equals("")){
            apeP.setError("Required");
        }else if (correo.equals("")){
            correoP.setError("Required");
        }else if (passowrd.equals("")){
            passowrdP.setError("Required");

        }

    }
}