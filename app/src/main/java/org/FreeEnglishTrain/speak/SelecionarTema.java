package org.FreeEnglishTrain.speak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.FreeEnglishTrain.database.Frase;
import org.FreeEnglishTrain.database.Tema;
import org.FreeEnglishTrain.database.BancoDeDados;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.FreeEnglishTrain.speak.R;
import org.FreeEnglishTrain.utils.Globals;

public class SelecionarTema extends Activity {
    private BancoDeDados mBancoDeDados;
    private ListView lvTema;
    private List<Tema> lstTema = new ArrayList<Tema>();
    private ArrayAdapter<Tema> arrAdapterTema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tema);
        inicializarComponentes();
        inicializarBancoDeDados();
        popularLista();
    }

    private void inicializarComponentes() {
        lvTema = (ListView) findViewById(R.id.lvTemas);
    }

    private void popularLista() {
        mBancoDeDados = new BancoDeDados(this);
        lstTema.clear();
        lstTema = mBancoDeDados.getAllTemas();
        arrAdapterTema = new ArrayAdapter<Tema>(this, android.R.layout.simple_list_item_1, lstTema);
        lvTema.setAdapter(arrAdapterTema);

        lvTema.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<Frase> lstFrase;
                lstFrase = mBancoDeDados.getFraseByTema(lstTema.get(i).id);
                Globals globals = (Globals) getApplicationContext();
                globals.setFrase(lstFrase);
                Intent in = new Intent(SelecionarTema.this, Treino.class);
                startActivity(in);
            }
        });
    }

    private void inicializarBancoDeDados() {
        mBancoDeDados = new BancoDeDados(this);
        File database = getApplicationContext().getDatabasePath(BancoDeDados.NOMEDB);
        if (!database.exists()) {
            mBancoDeDados.getReadableDatabase();
            if (mBancoDeDados.copiaBanco(this)) {
                alert("Banco copiado com sucesso");
            } else {
                alert("Erro ao copiar o banco de dados");
            }
        }
    }

    // ALERTA DE MENSAGEM COMO https://media.geeksforgeeks.org/wp-content/uploads/Toast.jpg
    private void alert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

}
