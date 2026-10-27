package com.projeto.simuladoac2;

import com.google.firebase.firestore.Exclude;
import java.io.Serializable;

public class Filme implements Serializable {

    // O @Exclude faz com que o ID não seja duplicado como um campo dentro do documento do Firestore
    @Exclude
    private String id;

    private String nome;
    private String tipo; // Filme, Série, Documentário, Animação[cite: 1]
    private String genero; // Ação, Comédia, Drama, Terror, Ficção Científica[cite: 1]
    private int anoLancamento;
    private double notaPessoal;
    private boolean jaAssistiu;

    // O Firestore exige um construtor vazio
    public Filme() {
    }

    public Filme(String nome, String tipo, String genero, int anoLancamento, double notaPessoal, boolean jaAssistiu) {
        this.nome = nome;
        this.tipo = tipo;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.notaPessoal = notaPessoal;
        this.jaAssistiu = jaAssistiu;
    }

    // Getters e Setters
    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public double getNotaPessoal() {
        return notaPessoal;
    }

    public void setNotaPessoal(double notaPessoal) {
        this.notaPessoal = notaPessoal;
    }

    public boolean isJaAssistiu() {
        return jaAssistiu;
    }

    public void setJaAssistiu(boolean jaAssistiu) {
        this.jaAssistiu = jaAssistiu;
    }
}
