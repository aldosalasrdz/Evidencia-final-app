package com.example.evidencia_final_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameInput,emailInput, passwordInput;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = (TextInputEditText) findViewById(R.id.register_name_input);
        emailInput = (TextInputEditText) findViewById(R.id.register_email_input);
        passwordInput = (TextInputEditText) findViewById(R.id.register_password_input);
        Button register = (Button) findViewById(R.id.register_button);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String name = nameInput.getText().toString().trim();
        final String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty()) {
            emailInput.setError("Coloca un correo electrónico");
            emailInput.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Coloca un correo electrónico válido");
            emailInput.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Coloca una contraseña");
            passwordInput.requestFocus();
            return;
        }

        if (password.length() <= 6) {
            passwordInput.setError("Coloca una contraseña mayor a 6 caracteres");
            passwordInput.requestFocus();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            final User user = new User(name, email);

                            FirebaseUser usernameInFirebase = auth.getCurrentUser();
                            String UserID = usernameInFirebase.getEmail();
                            String resultEmail = UserID.replace(".","");

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(resultEmail)
                                    .child("UserDetails")
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this,
                                                "Registro exitoso", Toast.LENGTH_LONG)
                                                .show();
                                        startActivity(new Intent(
                                                RegisterActivity.this,
                                                DashboardActivity.class)
                                        );
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registro fallido",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}