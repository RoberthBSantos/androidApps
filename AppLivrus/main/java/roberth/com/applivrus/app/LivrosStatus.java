package roberth.com.applivrus.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import roberth.com.applivrus.models.Livro;
import roberth.com.applivrus.R;

public class LivrosStatus extends AppCompatActivity {

    private TextView titulo;
    private TextView autor;
    private TextView genero;
    private TextView estado;
    private ProgressBar barraProgresso;
    private TextView progresso;
    private RatingBar pontuacao;
    private float calculoProgresso;
    private TextView comentario;
    private Livro livro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livros_status);

        titulo = (TextView) findViewById(R.id.tv_status_titulo);
        autor = (TextView) findViewById(R.id.tv_status_autor);
        genero = (TextView) findViewById(R.id.tv_status_genero);
        estado = (TextView) findViewById(R.id.tv_status_estado);
        barraProgresso = (ProgressBar) findViewById(R.id.pb_status);
        progresso = (TextView) findViewById(R.id.tv_status_progresso);
        pontuacao = (RatingBar) findViewById(R.id.rb_status);
        comentario = (TextView) findViewById(R.id.tv_status_comentario);

        buscaDadosLivro();

    }

    public void buscaDadosLivro(){
        //pegar o id do livro pela intent
        livro = new Livro();
        Intent intent = getIntent();
        long id = intent.getLongExtra("livro",0);
        livro = Livro.findById(Livro.class,id);

        //passando os dados do livro para os campos
        titulo.setText(livro.getTitulo().toString());

        //caso autor não existir tratar o NullPointer
        try {
            autor.setText("Autor: " + livro.getAutor().toString());
        }catch (NullPointerException npe){
            autor.setText("Autor: Desconhecido");
        }
        genero.setText("Gênero: " + livro.getGenero().toString());

        //Setando o texto de acordo com o id do spinner...
        try {
            if (Integer.valueOf(livro.getEstado()) == 0) {
                estado.setText("Estado: Na fila");
            }

            if (Integer.valueOf(livro.getEstado()) == 1) {
                estado.setText("Estado: Em leitura.");
            }

            if (Integer.valueOf(livro.getEstado()) == 2) {
                estado.setText("Estado: Em pausa");
            }

            if (Integer.valueOf(livro.getEstado()) == 3) {
                estado.setText("Estado: Concluído");
            }
        }catch(NumberFormatException nfe){
            estado.setText("Estado: Na fila");
        }


        calculoProgresso = (Integer.valueOf(livro.getPaginaAtual() * 100) / Integer.valueOf(livro.getPaginas()));
        barraProgresso.setProgress((int) calculoProgresso);
        progresso.setText(String.valueOf(livro.getPaginaAtual()) + "/" + String.valueOf(livro.getPaginas()));
        pontuacao.setRating(livro.getAvaliacao());
        comentario.setText(livro.getComentario());

    }


    public void editarDados(View view) {
        Intent intent = new Intent(LivrosStatus.this, DetalheLivroActivity.class);
        intent.putExtra("livro",livro.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        buscaDadosLivro();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        buscaDadosLivro();
        super.onRestart();
    }
}
