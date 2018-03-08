package roberth.com.applivrus.app;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import roberth.com.applivrus.models.Autor;
import roberth.com.applivrus.R;
import roberth.com.applivrus.adapters.ListaAutoresRVAdapter;

public class ListaAutoresActivity extends AppCompatActivity {
    RecyclerView rvAutores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_autores);


        rvAutores = (RecyclerView) findViewById(R.id.rv_autores);


    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAutores();
    }

    public void carregarAutores() {
        List<Autor> autores = Autor.listAll(Autor.class);

    ListaAutoresRVAdapter adapter = new ListaAutoresRVAdapter(this,autores);
        rvAutores.setAdapter(adapter);

        rvAutores.setLayoutManager(new LinearLayoutManager(this));
}

    public void novoAutor(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View viewDialog = getLayoutInflater().inflate(R.layout.activity_formulario_autor, null);

        builder.setView(viewDialog)
                .setTitle("Novo autor")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       EditText edAutorNome = (EditText) viewDialog.findViewById(R.id.ed_autor_nome);
                       EditText edAutorPais = (EditText) viewDialog.findViewById(R.id.ed_autor_pais);

                        String nome = edAutorNome.getText().toString();
                        String pais = edAutorPais.getText().toString();

                        Autor autor = new Autor(nome,pais);
                        if(autor.getNome().equals("")){
                            Toast.makeText(ListaAutoresActivity.this,"Falha ao cadastrar(Nome do autor é necessário)",Toast.LENGTH_LONG).show();
                        }else {
                            autor.save();
                            carregarAutores();
                        }
                    }
                })
                .setNegativeButton("Cancelar",null)
                .show();
    }


}
