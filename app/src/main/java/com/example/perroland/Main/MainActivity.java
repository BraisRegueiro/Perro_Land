package com.example.perroland.Main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.perroland.R;
import com.example.perroland.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    MisPerros frgMisPerros= new MisPerros();
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();

        db = FirebaseFirestore.getInstance();

        String doc = idDOcUserLOgueado();
        cargaDocumentoUsuarioLogueado(doc);

        changeFragmentMain(frgMisPerros);

    }

    private void initButtons() {

        ImageButton btnMisPerros= findViewById(R.id.act_main_misperros_btn);
        ImageButton btnBusqueda= findViewById(R.id.act_main_busqueda_btn);
        ImageButton btnPerfil = findViewById(R.id.act_main_perfil_btn);


        btnMisPerros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentMain(frgMisPerros);
            }
        });

        btnBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 EncontradosFragment frgEncontrados = new EncontradosFragment();
                changeFragmentMain(frgEncontrados);
            }
        });


        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerfilFragment frgPerfil = new PerfilFragment();
                changeFragmentMain(frgPerfil);
            }
        });

    }

    private void changeFragmentMain(MisPerros frgPerfil) {

    }

    public Usuario getUser(){
        final Gson gson = new Gson();

        String fileName = getString(R.string.sharedpreferences_file);
        SharedPreferences sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String profile = sharedPref.getString("profile", "{}");
        Usuario user = gson.fromJson(profile, Usuario.class);
        return user;
    }

    public String cargaemail(){
        Usuario user = this.getUser();
        return user.getEmail();
    }

    public String cargaUsername(){
        Usuario user = this.getUser();
        return user.getUsername();
    }

    public String cargaDescription(){
        Usuario user = this.getUser();
        return user.getDescription();
    }


    public String idDOcUserLOgueado(){
        String fileName = getString(R.string.sharedpreferences_file);
        SharedPreferences sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String busca = sharedPref.getString("id", "No existe");
        return busca;
    }

    public void cargaDocumentoUsuarioLogueado(String id){

        DocumentReference docRef = db.collection("users").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Usuario u = value.toObject(Usuario.class);
                saveOnShared(u);

            }
        });


    }


    private void saveOnShared(Usuario user) {

        if (user == null){
            return;
        }

        String fileName = getString(R.string.sharedpreferences_file);
        SharedPreferences sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        final Gson gson = new Gson();
        String jsonData = gson.toJson(user);
        editor.putString("profile", jsonData );

        editor.commit();

    }



    public void activity_main(Activity act) {
        Intent intent = new Intent(this, act.getClass());
        startActivity(intent);
    }
}