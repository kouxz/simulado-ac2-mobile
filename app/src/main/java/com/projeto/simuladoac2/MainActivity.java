package com.projeto.simuladoac2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnIrCadastro = findViewById(R.id.btnIrCadastro);
        Button btnIrListagem = findViewById(R.id.btnIrListagem);

        btnIrCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

        btnIrListagem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListagemActivity.class);
            startActivity(intent);
        });
    }
}