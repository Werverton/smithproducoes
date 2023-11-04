package com.example.smithproducoes.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class GettingIdFromUrlWithRegex {
    public static String gettingId(String url){

        // Defina o padrão da expressão regular
        Pattern pattern = Pattern.compile("/d/(.*?)/view");

        // Crie um objeto Matcher para encontrar a correspondência
        Matcher matcher = pattern.matcher(url);

        // Verifique se há uma correspondência
        if (matcher.find()) {
            // O valor entre "d/" e "/view" está no grupo de captura 1
            String valor = matcher.group(1);
            System.out.println("Valor extraído: " + valor);
            return valor;
        } else {
            System.out.println("Nenhuma correspondência encontrada");
        }
        return null;
    }

}
