package roberth.com.applivrus.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import roberth.com.applivrus.models.Autor;
import roberth.com.applivrus.R;

/**
 * Created by Roberth Santos on 17/02/2018.
 */

public class ListaAutoresRVAdapter extends RecyclerView.Adapter<ListaAutoresRVAdapter.ViewHolder> {

    private final Context context;
    private final List<Autor> autores;

    @Override
    public ListaAutoresRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_autores,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Autor autor = this.autores.get(position);
        holder.tvAutorNome.setText(autor.getNome());
        holder.tvAutorPais.setText(autor.getPais());

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Autor: " + autor.getNome(), Snackbar.LENGTH_LONG).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                PopupMenu pop = new PopupMenu(context, v);
                MenuInflater menuInflater = pop.getMenuInflater();

                menuInflater.inflate(R.menu.popup_menu_lista_livros, pop.getMenu());

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.item_remover_livro){
                            autor.delete();
                            ListaAutoresRVAdapter.this.autores.remove(position);
                            ListaAutoresRVAdapter.this.notifyItemRemoved(position);
                            ListaAutoresRVAdapter.this.notifyItemChanged(position,ListaAutoresRVAdapter.this.autores.size());
                            Toast.makeText(context,"Removido",Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                });

                pop.show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return autores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView tvAutorNome;
        protected TextView tvAutorPais;

        public ViewHolder(View itemView){
            super(itemView);

            tvAutorNome = (TextView) itemView.findViewById(R.id.tv_autor_nome);
            tvAutorPais = (TextView) itemView.findViewById(R.id.tv_autor_pais);
        }
    }
    public ListaAutoresRVAdapter(Context context, List<Autor> autores){

        this.context = context;
        this.autores = autores;
    }
}
