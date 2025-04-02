package org.intuitive.testenivelamento.services;
import org.intuitive.testenivelamento.utils.CompressToZip;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class WebScraper {
    private static final String URL = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
    private static final String DOWNLOAD_FOLDER = "downloads";
    private static final String ZIP_FILE_NAME = DOWNLOAD_FOLDER + "/anexos.zip";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static void execute(){
        try{
            File downloadDir = new File(WebScraper.DOWNLOAD_FOLDER);
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }

            Document doc = Jsoup.connect(URL).get();
            for (Element link : doc.select("a[href$=.pdf]")) {
                String pdfUrl = link.absUrl("href");
                String fileName = pdfUrl.substring(pdfUrl.lastIndexOf('/') + 1);

                if (fileName.contains("Anexo_I") || fileName.contains("Anexo_II")) {
                    baixarArquivo(pdfUrl, DOWNLOAD_FOLDER + "/" + fileName);
                }
            }

            CompressToZip.compactarArquivos(DOWNLOAD_FOLDER, ZIP_FILE_NAME);
            System.out.println("✅ Processo concluído com sucesso!");

        } catch (Exception e){
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private static void baixarArquivo(String fileURL, String savePath) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileURL))
                    .GET()
                    .build();

            HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());

            FileOutputStream fos = new FileOutputStream(savePath);
            InputStream inputStream = response.body();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            fos.close();
            inputStream.close();

            System.out.println("✅ Baixado: " + savePath);
        } catch (IOException | InterruptedException e) {
            System.out.println("❌ Erro ao baixar: " + fileURL);
        }
    }

}