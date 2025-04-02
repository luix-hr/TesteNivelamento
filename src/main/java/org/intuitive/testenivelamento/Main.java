package org.intuitive.testenivelamento;


import org.intuitive.testenivelamento.services.DataTransformation;
import org.intuitive.testenivelamento.services.WebScraper;

public class Main {
    public static void main(String[] args) {
        //Primeiro garanta que o Anexo I e II foram baixados
        WebScraper.execute();

        //Transformação dos dados
        DataTransformation.execute();
    }
}
