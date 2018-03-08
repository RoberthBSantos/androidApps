package roberth.com.applivrus.app;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import roberth.com.applivrus.models.Autor;
import roberth.com.applivrus.models.Livro;
import roberth.com.applivrus.R;
import roberth.com.applivrus.models.Sessao;
import roberth.com.applivrus.models.Usuario;

public class FormularioLivroActivity extends AppCompatActivity {

    private EditText edLivroTitulo;
    private EditText edLivroGenero;
    private EditText edLivroAno;
    private AutoCompleteTextView acLivroAutor;
    private EditText edLivroPaginas;
    private String estado = "";
    private Autor autorSelecionado;
    private Livro livro;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_livro);

        livro = new Livro();
        Intent intent = getIntent();

        edLivroTitulo = (EditText) findViewById(R.id.ed_livro_titulo);
        edLivroGenero = (EditText) findViewById(R.id.ed_livro_genero);
        edLivroAno = (EditText) findViewById(R.id.ed_livro_ano);
        acLivroAutor = (AutoCompleteTextView) findViewById(R.id.ac_livro_autor);
        edLivroPaginas = (EditText) findViewById(R.id.ed_livro_paginas);
        Sessao sessao = Sessao.listAll(Sessao.class).get(0);
        this.usuario = Usuario.findById(Usuario.class,sessao.getIdUsuario());

        acLivroAutor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                autorSelecionado = (Autor) parent.getItemAtPosition(position);

            }
        });

        // se vier id no getIntent vc substitui o livro

        long id = intent.getLongExtra("livro",0);
        if (id > 0){

            // Preencher os editext

            livro = Livro.findById(Livro.class,id);
            edLivroTitulo.setText(livro.getTitulo());
            edLivroGenero.setText(livro.getGenero());
            edLivroAno.setText(String.valueOf(livro.getAno()));
            try {
                acLivroAutor.setText(livro.getAutor().getNome()+" - "+livro.getAutor().getPais());
            }catch (NullPointerException npe){
                acLivroAutor.setText("");
            }
            edLivroPaginas.setText(String.valueOf(livro.getPaginas()));
            estado = livro.getEstado();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        carregarAutores();

    }

    private void carregarAutores() {

        List<Autor> autores = Autor.listAll(Autor.class);

        ArrayAdapter<Autor> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,autores);
        acLivroAutor.setAdapter(adapter);
    }


    public void salvarLivro(View view) {
        try {
            final String titulo = edLivroTitulo.getText().toString();
            final String genero = edLivroGenero.getText().toString();
            final int ano = Integer.valueOf(edLivroAno.getText().toString());
            final int paginas = Integer.valueOf(edLivroPaginas.getText().toString());

            if (autorSelecionado == null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final View viewDialog = getLayoutInflater().inflate(R.layout.activity_formulario_autor, null);

                builder.setView(viewDialog)
                        .setTitle("Autor deve ser salvo!")
                        .setPositiveButton("Salvar", (dialog, which) -> {

                            EditText edAutorNome = (EditText) viewDialog.findViewById(R.id.ed_autor_nome);
                            EditText edAutorPais = (EditText) viewDialog.findViewById(R.id.ed_autor_pais);

                            String nome = edAutorNome.getText().toString();
                            String pais = edAutorPais.getText().toString();

                            Autor autor = new Autor(nome, pais);
                            autor.save();
                            autorSelecionado = autor;

                            //Livro livro = new Livro(titulo, ano, autorSelecionado);
                            livro.setTitulo(titulo);
                            livro.setGenero(genero);
                            livro.setAno(ano);
                            livro.setAutor(autorSelecionado);
                            livro.setPaginas(paginas);
                            livro.setEstado(estado);
                            livro.setUsuario(this.usuario);
                            livro.save();
                            Toast.makeText(FormularioLivroActivity.this, "Livro salvo.", Toast.LENGTH_LONG).show();
                            finish();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();


            } else {

                //Livro livro = new Livro(titulo, ano, autorSelecionado);
                livro.setTitulo(titulo);
                livro.setGenero(genero);
                livro.setAno(ano);
                livro.setAutor(autorSelecionado);
                livro.setPaginas(paginas);
                livro.setEstado(estado);
                livro.setUsuario(this.usuario);
                livro.save();
                Toast.makeText(this, "Livro salvo.", Toast.LENGTH_LONG).show();
                finish();
            }

        }catch (Exception e){
            Toast.makeText(this,"Preencha as informações corretamente.",Toast.LENGTH_SHORT).show();

        }
    }

}
