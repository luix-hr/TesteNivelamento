package org.intuitive.testenivelamento.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.opencsv.CSVWriter;
import org.intuitive.testenivelamento.utils.CompressToZip;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;


public class DataTransformation {
    private static final String DOWNLOAD_FOLDER = "downloads";
    private static final String FILE_PATH = DOWNLOAD_FOLDER + "/Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf";
    private static final String CSV_PATH = DOWNLOAD_FOLDER + "/dados.csv";
    private static final String ZIP_FILE_NAME = DOWNLOAD_FOLDER + "/Teste_Luiz_Henrique.zip";

    public static void execute(){
        try {
            List<String[]> dadosCSV = extrairTextoPDF(FILE_PATH);
            salvarCSV(dadosCSV, CSV_PATH);
            CompressToZip.compactarArquivos(CSV_PATH, ZIP_FILE_NAME);

            System.out.println("✅ Arquivo ZIP gerado: " + ZIP_FILE_NAME);

        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<String[]> extrairTextoPDF(String caminho) throws IOException {
        try (PDDocument doc = PDDocument.load(new File(caminho))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return processarTexto(stripper.getText(doc));
        }
    }

    private static List<String[]> processarTexto(String texto) {
        return texto.lines()
                .map(line -> line.split("\\s{2,}")) // Divide colunas corretamente por espaços múltiplos
                .map(DataTransformation::substituirSiglas) // Substitui abreviações
                .collect(Collectors.toList());
    }

    private static String[] substituirSiglas(String[] linhas) {
        for (int i = 0; i < linhas.length; i++) {
            linhas[i] = linhas[i].replace("OD", "Seg. Odontológica")
                    .replace("AMB", "Seg. Ambulatorial");
        }
        return linhas;
    }

    private static void salvarCSV(List<String[]> dados, String caminho) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(caminho))) {
            String[] header = {"PROCEDIMENTO", "RN (alteração)", "VIGÊNCIA", "OD", "AMB", "HCO", "HSO", "REF", "PAC", "DUT", "SUBGRUPO", "GRUPO", "CAPÍTULO"};
            writer.writeNext(header);

            for (String[] row : dados) {
                writer.writeNext(row);
            }
        }
    }


}
