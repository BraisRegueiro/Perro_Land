package com.example.perroland.Auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.perroland.MainActivity;
import com.example.perroland.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText usuario, email, password, confirmPassword;

    // DatabaseReference mRootReference;
    FirebaseFirestore db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        usuario = v.findViewById(R.id.frg_register_nombre);
        email = v.findViewById(R.id.frg_register_email);
        password = v.findViewById(R.id.frg_register_pass);
        confirmPassword = v.findViewById(R.id.frg_register_confirmPass);



        Button btnSignIn = v.findViewById(R.id.frg_register_btn_entra);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroUser();

            }
        });

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {
        Log.i("User:", "" + currentUser);
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void registroUser() {

        String error = getString(R.string.datos_incorrectos);
        String error2 = getString(R.string.pass_corta);
        Toast toast = Toast.makeText(getContext(), error, Toast.LENGTH_SHORT);
        Toast toast1 = Toast.makeText(getContext(), error2, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);

        final String compruebaUser = usuario.getText().toString();
        if (compruebaUser.trim().isEmpty()) {
            toast.show();
            return;
        }

        final String compruebaEmail = email.getText().toString();
        if (compruebaEmail.trim().isEmpty()) {
            toast.show();
            return;
        }
        if (!validarEmail(compruebaEmail)) {
            toast.show();
            return;
        }

        final String compruebaPassword = password.getText().toString();
        if (compruebaPassword.trim().isEmpty()) {
            toast.show();
            return;
        }
        if (compruebaPassword.length() < 6) {
            toast1.show();
            return;
        }

        String compruebaConfirmPassword = confirmPassword.getText().toString();
        if (compruebaConfirmPassword.trim().isEmpty()) {
            toast.show();
            return;
        }

        if (!compruebaConfirmPassword.equals(compruebaPassword)) {
            toast.show();
            return;
        }

        creaUsuario(compruebaUser, compruebaEmail, compruebaPassword);


    }

    private void creaUsuario(final String user1, final String email1, final String pass1) {

        final String oki = "registrado";
        final String fallo = "caspitas";
        final Toast toast = Toast.makeText(getContext(), oki, Toast.LENGTH_SHORT);
        final Toast toast1 = Toast.makeText(getContext(), fallo, Toast.LENGTH_SHORT);

        mAuth.createUserWithEmailAndPassword(email1, pass1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d("OK", "Usuario Creado");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);
                            toast.show();
                            guardaDatos(user1, email1, currentUser);
                            shared(currentUser.getUid());
                           MainActivity actMain = new MainActivity();
                            ((MainActivity) getActivity()).activity_main(actMain);

                        } else {
                            Log.e("ERROR", "No se Ha creado el usuario");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI(null);
                            toast1.show();
                        }
                    }
                });
    }

    private void shared(String id) {

        String filename = "ficheroConfiguracion";
        Context ctx = getContext();
        SharedPreferences sharedPref = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("id", id);
        editor.commit();

    }

    private void guardaDatos(String username, String email, FirebaseUser user) {

        Map<String, Object> datosUsuario = new HashMap<>();
        datosUsuario.put("email", email);
        datosUsuario.put("username", username);

        db.collection("users").document(user.getUid())
                .set(datosUsuario);

    }

}