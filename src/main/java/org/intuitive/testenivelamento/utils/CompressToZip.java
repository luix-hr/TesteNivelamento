package org.intuitive.testenivelamento.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressToZip {
    public static void compactarArquivos(String folderPath, String zipFilePath) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);

            File dir = new File(folderPath);
            File[] files = dir.listFiles();

            if (files != null) {
                for (File file : files) {
                    FileInputStream fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(file.getName()));

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, bytesRead);
                    }

                    zos.closeEntry();
                    fis.close();
                }
            }

            zos.close();
            fos.close();
            System.out.println("ZIP salvo em: " + zipFilePath);
        } catch (IOException e) {
            System.out.println("Erro ao compactar arquivos.");
        }
    }
}
