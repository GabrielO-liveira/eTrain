package org.FreeEnglishTrain.utils;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

//TODO:
// Falta realizar
// Validação de Bom-Regular-Ruim.
// Fazer o "Home Sweet Home" Funcionar.
// Colocar a ListView do Reforço.

public class ValidaFrase {
   private SimpleCallback fraseDita;
   private SimpleCallback palavraDita;
   StringUtils su = new StringUtils();
   String frase;
   String lastWordDetected;
   List palavrasDeContagem;
   List palavrasADizer;
   int lastPosition;

   private static final double CONFIDANCE_OTIMO = 0.75;
   private static final double CONFIDANCE_REGULAR = 0.6;
   private static final String FORMAT_1 = "<font color='#00FF00'>";
   private static final String FORMAT_2 = "</font>";
   private static final String DF_1 = "<font color='#B0B0B0'>";
   private static final String DF_2 = "</font>";

   /*
    *   public void onCreate() {
    *      lastWordDetected = "";
    *      palavrasDeContagem = new ArrayList();
    *      palavrasADizer = new ArrayList();}
    */

   public void novoCiclo(String _frase) {
      frase = _frase;
      lastWordDetected = "";
      lastPosition = 0;
      palavrasDeContagem = new ArrayList();
      palavrasADizer = new ArrayList();
      //good morning ->[good,morning]
      palavrasADizer.addAll(Arrays.asList(su.limpaPontuacao(frase).toLowerCase(Locale.ROOT).split(" ")));
   }


   // FRASE DITA COMPLETAMENTE
   private void onPhraseSayed() {
      fraseDita.method();
   }

   public void setOnPhaseSayed(SimpleCallback _method) {
      fraseDita = _method;
   }

   // NOVA PALAVRA RECONHECIDA ESPERADA E DITA
   public void setOnExpectedWordSayed(SimpleCallback _method) {
      palavraDita = _method;
   }

   private void onExpectedWordSayed() {
      palavraDita.method();
   }

   // Junta palavrasADizer em uma string e passa junto com as palavrasDitas para pintar na tela
   // (x)Pega a palacra que foi dita, quebra e adiciona em uma lista unica synchronized
   public void trataRetornoVosk(String _frase) {

      String[] fraseSplitVosk = _frase.split(" ");
      for (int i = 0; i < fraseSplitVosk.length; i++) {
         if (fraseSplitVosk[i].length() == 0)
            return;

         if (!palavrasDeContagem.contains(fraseSplitVosk[i]))
            palavrasDeContagem.add(fraseSplitVosk[i]);

         //erro do home sweet home
         Log.i("trataRetornoVosk ", String.format("lastWordDetected = %s", lastWordDetected));
         Log.i("trataRetornoVosk ", String.format("fraseSplitVosk[i] = %s", fraseSplitVosk[i]));
         if (palavrasADizer.size() > 0) {
            if (palavrasADizer.get(0).equals(fraseSplitVosk[i])) {
               palavrasADizer.remove(0);
               lastWordDetected = fraseSplitVosk[i];
               onExpectedWordSayed();
               // log
               Log.i("trataRetornoVosk ", String.format("palavra = %s", fraseSplitVosk[i]));
            }
         }
         if (palavrasADizer.size() == 0) {
            onPhraseSayed();
         }
      }
   }

   public Spanned getPaintedtoText() {
      String Separador = "-----------------------------";
      String F1;
      String F2;
      Log.v("egram --", "METODO getPaintedtoText");
      Log.v("egram --", Separador);
      Log.v("egram --", String.format("lastWordDetected = %s", lastWordDetected));

      String P = lastWordDetected;
      Log.v("egram --", String.format("P = %s", P));
      Log.v("egram -- ", String.valueOf(lastPosition));

      int Pos = (frase.toLowerCase(Locale.ROOT).indexOf(P, lastPosition));
      Log.v("egram --", String.format("Pos = %d", Pos));
      int PosFinal = Pos + P.length();
      Log.v("egram --", String.format("PosFinal = %d", PosFinal));
      Log.v("egram --", String.format("Pos = %d", Pos));
      Log.v("egram --", String.format("P.length() = %d", P.length()));

      lastPosition = PosFinal;
      Log.v("egram --", String.format("lastPosition = %d", lastPosition));

      F1 = frase.substring(0, PosFinal);
      Log.v("egram --", String.format("F1 = %s", F1));

      F2 = frase.substring(PosFinal);
      Log.v("egram --", String.format("F2 = %s", F2));

      Log.v("egram --", Separador + "\n \n");
      //->LOG<-//

      // HOW ARE, YOU?
      return Html.fromHtml(FORMAT_1 + F1 + " " + FORMAT_2 + DF_1 + " " + F2 + DF_2);

   }


   public int nivelAcerto() {
      double d = getConfidance();
      Log.v("egram --", String.format("d = %s", d));
      int i = 1;
      if (d > CONFIDANCE_REGULAR) i = 2;
      if (d > CONFIDANCE_OTIMO) i = 3;
      return i;
   }

   public double getConfidance() {
      int wordsToSay = Arrays.asList(su.limpaPontuacao(frase).split(" ")).size();
      int sayedWords = palavrasDeContagem.size();
      if (sayedWords == 0) return 0;
      Log.v("egram --", String.format("sayedWords = %d", sayedWords));
      Log.v("egram --", String.format("wordsToSay = %d", wordsToSay));
      return ((double)wordsToSay /(double)sayedWords  );

   }

   public void getStateLog() {
      Log.i("egraml palavrasDoVosk", lastWordDetected);
      Log.i("egraml palavrasADizer", palavrasADizer.toString());
      Log.i("egraml palavrasDeCont", palavrasDeContagem.toString());
      if (palavrasDeContagem.size() != 0) {
         String nivelAcerto = String.format("%d", nivelAcerto());
         Log.i("egraml nivelAcerto", nivelAcerto);
      }
   }
}
