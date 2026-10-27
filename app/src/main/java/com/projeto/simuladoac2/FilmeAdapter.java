package com.projeto.simuladoac2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FilmeAdapter extends RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder> {

    private List<Filme> listaFilmes;
    private OnItemClickListener listener;

    // Interface para lidar com os cliques
    public interface OnItemClickListener {
        void onItemClick(Filme filme); // Clique curto
        void onItemLongClick(Filme filme); // Clique longo
    }

    public FilmeAdapter(List<Filme> listaFilmes, OnItemClickListener listener) {
        this.listaFilmes = listaFilmes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilmeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filme, parent, false);
        return new FilmeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmeViewHolder holder, int position) {
        Filme filme = listaFilmes.get(position);

        // Exibir nome, tipo, gênero, ano de lançamento e nota pessoal
        holder.textNome.setText(filme.getNome());
        holder.textTipo.setText("Tipo: " + filme.getTipo());
        holder.textGenero.setText("Género: " + filme.getGenero());
        holder.textAno.setText("Ano: " + filme.getAnoLancamento());
        holder.textNota.setText("Nota: " + filme.getNotaPessoal());

        // Configurar os cliques
        holder.itemView.setOnClickListener(v -> listener.onItemClick(filme));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(filme);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaFilmes.size();
    }

    // Método para atualizar a lista quando usar o filtro
    public void setListaFilmes(List<Filme> novaLista) {
        this.listaFilmes = novaLista;
        notifyDataSetChanged();
    }

    static class FilmeViewHolder extends RecyclerView.ViewHolder {
        TextView textNome, textTipo, textGenero, textAno, textNota;

        public FilmeViewHolder(@NonNull View itemView) {
            super(itemView);
            textNome = itemView.findViewById(R.id.textNomeItem);
            textTipo = itemView.findViewById(R.id.textTipoItem);
            textGenero = itemView.findViewById(R.id.textGeneroItem);
            textAno = itemView.findViewById(R.id.textAnoItem);
            textNota = itemView.findViewById(R.id.textNotaItem);
        }
    }
}
