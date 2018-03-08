package roberth.com.applivrus.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import roberth.com.applivrus.models.Livro;
import roberth.com.applivrus.R;

public class DetalheLivroActivity extends AppCompatActivity {
    private TextView titulo;
    private Spinner estado;
    private Livro livro;
    private RatingBar avaliacao;
    private TextView tvComentario;
    private EditText edComentario;
    private EditText edPagina;
    private Button comentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_livro);

        titulo = (TextView) findViewById(R.id.tvl_detalhe_nome);
        estado = (Spinner) findViewById(R.id.d_spinner);
        edPagina = (EditText) findViewById(R.id.ed_detalhe_pagina);
        avaliacao = (RatingBar) findViewById(R.id.rb_detalhe);
        tvComentario = (TextView) findViewById(R.id.tv_detalhe_comentario);
        edComentario = (EditText) findViewById(R.id.ed_detalhe_comentario);
        comentar = (Button) findViewById(R.id.bt_detalhe_comentar);
        estado = (Spinner) findViewById(R.id.d_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.spinner,android.R.layout.simple_spinner_item);
        estado.setAdapter(adapter);

        livro = new Livro();
        Intent intent = getIntent();

        long id = intent.getLongExtra("livro",0);
        livro = Livro.findById(Livro.class,id);
        titulo.setText(livro.getTitulo().toString());
        if(livro.getPaginaAtual() == 0){
            edPagina.setText("");
        }else {
            edPagina.setText(String.valueOf(livro.getPaginaAtual()));
        }
        avaliacao.setRating(livro.getAvaliacao());
        try {
            tvComentario.setText(livro.getComentario().toString());
        }catch (NullPointerException npe){
            tvComentario.setText("");
        }

        try {
            estado.setSelection(Integer.valueOf(livro.getEstado()));
        }catch (NumberFormatException nfe){
            estado.setSelection(0);
        }



    }

    public void comentarLivro(View view) {
        tvComentario.setText(edComentario.getText().toString());
        livro.setComentario(edComentario.getText().toString());
        edComentario.setText("");
        livro.save();
    }

    public void salvarEstado(View view) {
        livro.setAvaliacao((int) avaliacao.getRating());
        try {
            livro.setPaginaAtual(Integer.parseInt(edPagina.getText().toString()));
        }catch (NumberFormatException e){
            livro.setPaginaAtual(0);
        }
        livro.setEstado(String.valueOf(estado.getSelectedItemId()));
        livro.save();
        Toast.makeText(this, "Salvo.", Toast.LENGTH_LONG).show();

    }
}
