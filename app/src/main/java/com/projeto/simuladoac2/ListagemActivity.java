package com.projeto.simuladoac2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListagemActivity extends AppCompatActivity {

    private EditText editBuscaNome; // Novo componente para busca
    private Spinner spinnerFiltro;
    private RecyclerView recyclerFilmes;
    private FilmeAdapter adapter;
    private List<Filme> listaOriginal = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem);

        db = FirebaseFirestore.getInstance();
        editBuscaNome = findViewById(R.id.editBuscaNome);
        spinnerFiltro = findViewById(R.id.spinnerFiltroTipo);
        recyclerFilmes = findViewById(R.id.recyclerFilmes);

        configurarRecyclerView();
        configurarFiltroSpinner();
        configurarBuscaPorNome();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarDadosDoFirebase();
    }

    private void configurarBuscaPorNome() {
        // Ouve cada letra digitada para atualizar a lista em tempo real[cite: 2]
        editBuscaNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltros();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void aplicarFiltros() {
        String tipoSelecionado = spinnerFiltro.getSelectedItem() != null ? spinnerFiltro.getSelectedItem().toString() : "Todos";
        String textoBusca = editBuscaNome.getText().toString().trim().toLowerCase();

        List<Filme> listaFiltrada = new ArrayList<>();

        for (Filme f : listaOriginal) {
            boolean correspondeTipo = tipoSelecionado.equals("Todos") || f.getTipo().equals(tipoSelecionado);
            boolean correspondeNome = f.getNome().toLowerCase().contains(textoBusca); // Extra de busca por nome[cite: 2]

            // Se bater com o tipo do Spinner E contiver o texto digitado, adiciona na lista
            if (correspondeTipo && correspondeNome) {
                listaFiltrada.add(f);
            }
        }
        adapter.setListaFilmes(listaFiltrada);
    }

    // --- OS MÉTODOS ABAIXO CONTINUAM EXATAMENTE IGUAIS AO PASSO ANTERIOR ---

    private void configurarRecyclerView() {
        recyclerFilmes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FilmeAdapter(new ArrayList<>(), new FilmeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Filme filme) {
                Intent intent = new Intent(ListagemActivity.this, CadastroActivity.class);
                intent.putExtra("filmeSelecionado", filme);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(Filme filme) {
                exibirDialogoExclusao(filme);
            }
        });
        recyclerFilmes.setAdapter(adapter);
    }

    private void exibirDialogoExclusao(Filme filme) {
        new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Tem certeza que deseja excluir o registo '" + filme.getNome() + "'?")
                .setPositiveButton("Sim", (dialog, which) -> excluirFilme(filme.getId()))
                .setNegativeButton("Não", null)
                .show();
    }

    private void excluirFilme(String idDocumento) {
        db.collection("obras").document(idDocumento)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ListagemActivity.this, "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
                    carregarDadosDoFirebase();
                })
                .addOnFailureListener(e -> Toast.makeText(ListagemActivity.this, "Erro ao excluir: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void configurarFiltroSpinner() {
        String[] tiposFiltro = {"Todos", "Filme", "Série", "Documentário", "Animação"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tiposFiltro);
        spinnerFiltro.setAdapter(adapterSpinner);

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aplicarFiltros(); // Alterado para chamar a nova função que combina os filtros
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void carregarDadosDoFirebase() {
        db.collection("obras").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listaOriginal.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Filme filme = document.toObject(Filme.class);
                    filme.setId(document.getId());
                    listaOriginal.add(filme);
                }
                aplicarFiltros(); // Aplica todos os filtros assim que os dados carregam
            } else {
                Toast.makeText(this, "Erro ao carregar dados.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}