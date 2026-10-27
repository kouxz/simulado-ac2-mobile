package com.projeto.simuladoac2;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome, editAno, editNota;
    private Spinner spinnerTipo, spinnerGenero;
    private CheckBox checkAssistiu;
    private Button btnSalvar;

    private FirebaseFirestore db;
    private String idFilmeEditado = null; // Variável para controlar se é uma edição

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        db = FirebaseFirestore.getInstance();

        editNome = findViewById(R.id.editNome);
        editAno = findViewById(R.id.editAno);
        editNota = findViewById(R.id.editNota);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        checkAssistiu = findViewById(R.id.checkAssistiu);
        btnSalvar = findViewById(R.id.btnSalvar);

        configurarSpinners();

        // Verificar se recebemos um Filme para editar[cite: 1]
        Filme filmeParaEditar = (Filme) getIntent().getSerializableExtra("filmeSelecionado");
        if (filmeParaEditar != null) {
            preencherCamposParaEdicao(filmeParaEditar);
        }

        btnSalvar.setOnClickListener(v -> salvarRegistro());
    }

    private void configurarSpinners() {
        String[] tipos = {"Selecione um tipo", "Filme", "Série", "Documentário", "Animação"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos);
        spinnerTipo.setAdapter(adapterTipo);

        String[] generos = {"Selecione um gênero", "Ação", "Comédia", "Drama", "Terror", "Ficção Científica"};
        ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, generos);
        spinnerGenero.setAdapter(adapterGenero);
    }

    private void preencherCamposParaEdicao(Filme filme) {
        idFilmeEditado = filme.getId(); // Guarda o ID para atualizar o registo correto no Firebase
        editNome.setText(filme.getNome());
        editAno.setText(String.valueOf(filme.getAnoLancamento()));
        editNota.setText(String.valueOf(filme.getNotaPessoal()));
        checkAssistiu.setChecked(filme.isJaAssistiu());

        // Selecionar os valores corretos nos Spinners
        selecionarValorSpinner(spinnerTipo, filme.getTipo());
        selecionarValorSpinner(spinnerGenero, filme.getGenero());

        btnSalvar.setText("Atualizar");
    }

    // Método auxiliar para descobrir a posição da String no Spinner
    private void selecionarValorSpinner(Spinner spinner, String valor) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(valor)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void salvarRegistro() {
        String nome = editNome.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String genero = spinnerGenero.getSelectedItem().toString();
        String anoTexto = editAno.getText().toString().trim();
        String notaTexto = editNota.getText().toString().trim();
        boolean jaAssistiu = checkAssistiu.isChecked();

        // Validações
        if (nome.isEmpty()) { editNome.setError("O nome não pode estar vazio"); editNome.requestFocus(); return; }
        if (tipo.equals("Selecione um tipo")) { Toast.makeText(this, "Selecione o tipo", Toast.LENGTH_SHORT).show(); return; }
        if (genero.equals("Selecione um gênero")) { Toast.makeText(this, "Selecione o gênero", Toast.LENGTH_SHORT).show(); return; }
        if (anoTexto.isEmpty()) { editAno.setError("O ano não pode estar vazio"); editAno.requestFocus(); return; }
        if (notaTexto.isEmpty()) { editNota.setError("A nota não pode estar vazia"); editNota.requestFocus(); return; }

        int ano = Integer.parseInt(anoTexto);
        double nota = Double.parseDouble(notaTexto);

        Filme filme = new Filme(nome, tipo, genero, ano, nota, jaAssistiu);

        if (idFilmeEditado == null) {
            // CRIAR NOVO REGISTO (Operação de Cadastro)[cite: 1]
            db.collection("obras").add(filme)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        finish(); // Fecha a ecrã e volta para a listagem
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show());
        } else {
            // ATUALIZAR REGISTO EXISTENTE (Operação de Edição)[cite: 1]
            db.collection("obras").document(idFilmeEditado).set(filme)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish(); // Fecha a ecrã e volta para a listagem
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }
}