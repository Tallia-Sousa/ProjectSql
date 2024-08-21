package com.example.sqlprojetc;

import static com.example.sqlprojetc.R.id.activity_main;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sqlprojetc.Database.Database;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private Database databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Inicializar os campos
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Inicializar o banco de dados
        databaseHelper = new Database(this);

        databaseHelper = new Database(this);

        // Configurar o evento do botão
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = databaseHelper.addUser(name, email, password);
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Registro realizado com sucesso", Toast.LENGTH_SHORT).show();

                    // Obter e exibir os dados do banco de dados, sempre sendo o ultimo registro que foi adicionado
                    Cursor cursor = databaseHelper.getAllUsers();
                    if (cursor.moveToLast()) { // Move o cursor para o último registro
                        @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex("name"));
                        @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex("email"));
                        Toast.makeText(MainActivity.this, "Último usuário: " + userName + " (" + userEmail + ")", Toast.LENGTH_LONG).show();
                    }
                    cursor.close(); // Fechar o cursor
                } else {
                    // se nao tiver usuario, a consulta nao registra nada
                    Toast.makeText(MainActivity.this, "Erro ao registrar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}