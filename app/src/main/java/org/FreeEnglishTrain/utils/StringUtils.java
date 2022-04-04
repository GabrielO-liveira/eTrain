package org.FreeEnglishTrain.utils;


import org.FreeEnglishTrain.database.Frase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StringUtils {

   public String frasesToGram(List<Frase> _lstFrase) {
      String Gram = "";
      String rs = "";
      for (int i = 0; i < _lstFrase.size(); i++) {
         //  rs = rs + " " + _lstFrase.get(i).frase.replace("?", " ").replace(".", " ").replace("  ", " ");

         rs = rs + " " + limpaPontuacao(_lstFrase.get(i).frase);
      }

      List<String> lstSepara = new ArrayList<>();
      Collections.addAll(lstSepara, rs.toLowerCase(Locale.ROOT).split(" "));
      Collections.sort(lstSepara);
      for (int j = 0; j < lstSepara.size(); j++)
         //if (!stringInList(Gram, lstSepara.get(j)) && (!lstSepara.get(j).equals(" ")))
            if (!Gram.contains("\"" + lstSepara.get(j) + "\"")
                    && (!limpaPontuacao(lstSepara.get(j)).equals("\t "))
                    && (!limpaPontuacao(lstSepara.get(j)).equals(" "))
                    && (!limpaPontuacao(lstSepara.get(j)).equals(""))
            )
            Gram = Gram + "\"" + lstSepara.get(j) + "\", ";
      Gram = Gram + "\"[unk]\"";
      return Gram;
   }

   public boolean stringInList(List<String> lista, String s) {
      for (int j = 0; j < lista.size(); j++)
         if (lista.get(j).equals(s)) {
            return true;
         }
      return false;
   }

   public String arraytoString(String separator, List<String> input) {
      if (input == null || input.size() <= 0) return "";
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < input.size(); i++) {
         sb.append(input.get(i));
         // if not the last item
         if (i != input.size() - 1) {
            sb.append(separator);
         }
      }
      return notNull(sb.toString());
   }

   public String notNull(String _s) {
      if (_s == null)
         return "";
      else
         return _s;
   }

//PROBELMS HERE
   public String limpaPontuacao(String _frase) {
      return _frase.replace("?", " ").replace(".", " ").replace("!", " ")
              .replace("-", " ").replace(",", " ").replace("'", " ").replace("\"", " ").replace("  ", " ");
   }
//Chamar função -> stringUtils.limpaPontuacao(lstFrase,indiceFrase);

}

