package org.FreeEnglishTrain.speak;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.FreeEnglishTrain.database.BancoDeDados;
import org.FreeEnglishTrain.utils.ValidaFrase;
import org.json.JSONException;
import org.json.JSONObject;
import org.vosk.Recognizer;
import org.vosk.android.RecognitionListener;
import org.vosk.android.SpeechService;
import org.FreeEnglishTrain.database.Frase;
import org.FreeEnglishTrain.utils.Globals;

import java.io.IOException;
import java.util.List;

import org.FreeEnglishTrain.utils.StringUtils;

public class Treino extends Activity implements RecognitionListener {
   private BancoDeDados mBancoDeDados;
   private SpeechService speechService;
   private Recognizer rec;
   private TextView resultView;
   private TextView txfrases;
   boolean velocidadeNormal = false;
   private List<Frase> lstFrase;
   Globals globals;
   StringUtils stringUtils = new StringUtils();
   Button btnControleAudio;
   Button btnSkip;
   ProgressBar barConfidance;
   ProgressBar barProgresso;
   ValidaFrase validaFrase = new ValidaFrase();
   MediaPlayer mpSuccess = new MediaPlayer();
   MediaPlayer mpError = new MediaPlayer();
   MediaPlayer mpSignal = new MediaPlayer();
   int indiceFrase;
   int indiceProgresso;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.v("egram ENTROU NO METODO:", " ONCREATE");
      setContentView(R.layout.activity_treino);
      setScreenItens();
      globals = (Globals) getApplicationContext();
      indiceFrase = 0;
      lstFrase = globals.getFrase();
      String gram = stringUtils.frasesToGram(lstFrase);
      mBancoDeDados = new BancoDeDados(this);
      recognizeMicrophone(gram);
      // Verifica permissoes e solicita acesso
      // A cada loop de interacao
      validaFrase.setOnPhaseSayed(this::onFalouTodasPalavras);
      validaFrase.setOnExpectedWordSayed(this::onPalavraFoiAcertada);
      InicializaLoop(0);
      indiceProgresso  = 100 / lstFrase.size();
      pintaProgresso();
      prepMediaPlayer();
   }

   public void onPalavraFoiAcertada() {
      Log.v("egram ENTROU NO METODO:", " PALAVRA FOI ACERTADA" + "\n \n");
      txfrases.setText(validaFrase.getPaintedtoText());
   }

   public void onFalouTodasPalavras() {
      Log.v("egram ENTROU NO METODO:", " FALOU TODAS PALAVRAS");
      speechService.setPause(true);
      rec.reset();
      notificaNivel(validaFrase.nivelAcerto());
      if (indiceFrase < lstFrase.size()) {
         mBancoDeDados.atualizaConfianca(lstFrase.get(indiceFrase).id, validaFrase.getConfidance());
         indiceFrase = indiceFrase + 1;
      }
      Handler handler = new Handler();
      handler.postDelayed(() -> {
         InicializaLoop(1);
         pintaBarra(0);
         if (isLastPhrase()) {
            Intent in = new Intent(Treino.this, Congratulations.class);
            startActivity(in);
            finish();
         }
      }, 1000);
   }


   private boolean isLastPhrase() {
      return (indiceFrase >= lstFrase.size());
   }

   private void setScreenItens() {
      txfrases = findViewById(R.id.txvResultado);
      barConfidance = findViewById(R.id.barConfidanceOtimo);
      barProgresso = findViewById(R.id.barProgresso);
      btnControleAudio = findViewById(R.id.btnControleAudio);
      resultView = findViewById(R.id.txvFrase);
      btnSkip = findViewById(R.id.btnSkip);
      btnControleAudio.setOnClickListener(v -> {
         try {
            speechService.setPause(true);
            globals.FalaAe(lstFrase.get(indiceFrase).frase, velocidadeNormal);
            velocidadeNormal = !velocidadeNormal;
            RestartListen();
         } catch (Exception e) {
            e.printStackTrace();
         }
      });
      btnSkip.setOnClickListener(v -> {
         ++indiceFrase;
         InicializaLoop(1);
      });
   }

   private void RestartListen() {
      Handler handler = new Handler();
      handler.postDelayed(() -> {
         if (globals.FalaeIsReady()) {
            speechService.setPause(false);
            mpSignal.start();
         } else {
            RestartListen();
         }
      }, 500);
   }

   private void pintaProgresso() {
   barProgresso.setProgress(indiceProgresso);
   }

   @Override
   public void onResult(String hypothesis) {

      String frase;
      try {
         JSONObject jsonObject;
         jsonObject = new JSONObject(hypothesis);
         //frase = jsonObject.getJSONArray("alternatives").getJSONObject(0).getString("text");
         frase = jsonObject.get("text").toString();
         if (!frase.equals("")) {
            Log.i("egram onResult", frase);
            validaFrase.trataRetornoVosk(frase);
         }
      } catch (JSONException e) {
         e.printStackTrace();
         Log.i("egram error", hypothesis);
      }
   }

   @Override
   public void onFinalResult(String hypothesis) {
   }

   @Override
   public void onPartialResult(String hypothesis) {
      String frase;
      try {
         JSONObject jsonObject = new JSONObject(hypothesis);
         frase = jsonObject.getString("partial");
         if (!frase.equals("")) {
            Log.i("egram onPartialResult", frase);
            validaFrase.trataRetornoVosk(frase);
         }
      } catch (JSONException e) {
         e.printStackTrace();
         Log.i("egram error", hypothesis);
      }
   }

   @Override
   public void onError(Exception e) {
      setErrorState(e.getMessage());
   }

   @Override
   public void onTimeout() {
      Log.i("egram palavrasDitas", "==> onTimeout");
   }

   private void setErrorState(String message) {
   }

   private void recognizeMicrophone(String Gram) {
      Gram = Gram.toLowerCase();
      try {
         String lGram = String.format("[%s]", Gram);
         Log.i("egram gram", lGram);
         rec = new Recognizer(globals.model, 16000.0f, lGram); // EXCEPTION
         speechService = new SpeechService(rec, 16000.0f);
         //rec.setMaxAlternatives(3);
         rec.reset();
         speechService.startListening(this);
      } catch (IOException e) {
         setErrorState(e.getMessage());
      }
   }

   public void notificaNivel(int nivel) {
      pintaBarra(nivel);
      if (nivel == 1)
         mpError.start();
      else
         mpSuccess.start();
   }


   public void prepMediaPlayer() {
      try {
         AssetFileDescriptor dsAcerto = getAssets().openFd("acerto.mp3");
         mpSuccess.setDataSource(dsAcerto.getFileDescriptor(), dsAcerto.getStartOffset(), dsAcerto.getLength());
         dsAcerto.close();
         mpSuccess.prepare();

         AssetFileDescriptor dsErro = getAssets().openFd("erro.mp3");
         mpError.setDataSource(dsErro.getFileDescriptor(), dsErro.getStartOffset(), dsErro.getLength());
         dsErro.close();
         mpError.prepare();

         AssetFileDescriptor dsSinal = getAssets().openFd("Signal.mp3");
         mpSignal.setDataSource(dsSinal.getFileDescriptor(), dsSinal.getStartOffset(), dsSinal.getLength());
         dsSinal.close();
         mpSignal.prepare();

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void pintaBarra(int _nivel) {
      if (_nivel == 1) {
         barConfidance.setProgress(33);
         barConfidance.setProgressTintList(ColorStateList.valueOf(Color.rgb(216, 56, 67)));
      } else if (_nivel == 2) {
         barConfidance.setProgress(66);
         barConfidance.setProgressTintList(ColorStateList.valueOf(Color.rgb(238, 159, 42)));
      } else if (_nivel == 3) {
         barConfidance.setProgress(100);
         barConfidance.setProgressTintList(ColorStateList.valueOf(Color.rgb(86, 200, 48)));
      }
   }

   public void InicializaLoop(int _inc) {
      //foi
      Log.v("egram InicializaLoop", "ENTROU NO METODO: INICIALIZALOOP");
      if (indiceFrase < 0 || isLastPhrase())
         return;
      validaFrase.novoCiclo(lstFrase.get(indiceFrase).frase);
      txfrases.setText(lstFrase.get(indiceFrase).frase);
      resultView.setText(lstFrase.get(indiceFrase).traducao);
      rec.reset();
      velocidadeNormal = false;
      indiceProgresso = indiceProgresso + 10;
      pintaProgresso();
      try {
         speechService.setPause(true);
         globals.FalaAe(lstFrase.get(indiceFrase).frase, velocidadeNormal);
         RestartListen();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public void Stop() {
      if (speechService != null) {
         speechService.stop();
         speechService.shutdown();
      }
      mpSuccess.stop();
      mpError.stop();
      mpSignal.stop();
   }

   //Pausa os serviços do speech
   @Override
   public void onDestroy() {
      this.Stop();
      super.onDestroy();

   }

}


