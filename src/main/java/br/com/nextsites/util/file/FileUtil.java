package br.com.nextsites.util.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 25/05/2020 - 21:12
 */

public class FileUtil {

    public static final String SEPARADOR = System.getProperty("file.separator");
    private static final String PRINCIPAL = System.getProperty("user.home")+SEPARADOR+"SGA"+SEPARADOR;

    public static List<File> listaPastasNoDiretorio(String diretorio, String startNome){
        File dir = new File(PRINCIPAL+diretorio);
        List<File> retorno = new ArrayList<>();
        if(dir.exists()){
            File[] files = dir.listFiles();
            for(File arquivo : files){
                if(arquivo.isDirectory() && arquivo.getName().startsWith(startNome)){
                    retorno.add(arquivo);
                }
            }
        }else{
            dir.mkdirs();
        }
        return retorno;
    }

    public void gravarArquivo(final String arquivo, final byte[] conteudo) throws IOException {
        String caminho = PRINCIPAL+arquivo;
        File file = new File(caminho);
        OutputStream out = new FileOutputStream(file);
        out.write(conteudo);
        out.close();
        file.createNewFile();
    }

    public void gravarArquivoTxt(final String arquivo, final String conteudo) throws IOException {
        FileWriter fileWriter = new FileWriter(PRINCIPAL+arquivo);
        PrintWriter gravar = new PrintWriter(fileWriter);
        gravar.write(conteudo);
        fileWriter.close();
        gravar.close();
    }

    public String getConteudo(String enderecoRecurso) throws IOException {
        File f = new File(PRINCIPAL+enderecoRecurso);

        PDDocument pdfDocument = null;
        try{
            org.apache.pdfbox.io.RandomAccessFile raf = new org.apache.pdfbox.io.RandomAccessFile(f, "r");
            PDFParser parser = new PDFParser(raf);
            parser.parse();
            pdfDocument = parser.getPDDocument();
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(pdfDocument);
        } finally {
            if (pdfDocument != null){
                pdfDocument.close();
            }
        }
    }

    public void deletarArquivo(String diretorio){
        File file = new File(PRINCIPAL+diretorio);
        file.delete();
    }

    public boolean contemTexto(String diretorio, String texto){

        try{
            FileReader fileReader = new FileReader(PRINCIPAL+diretorio);
            BufferedReader lerArq = new BufferedReader(fileReader);
            String linha = lerArq.readLine();
            if(linha.contains(texto)){
                lerArq.close();
                fileReader.close();
                return true;
            }

            StringBuilder conteudo = new StringBuilder(linha);
            while (linha != null){
                linha = lerArq.readLine();
                if(StringUtils.isNotBlank(linha)){
                    if(linha.contains(texto)){
                        lerArq.close();
                        fileReader.close();
                        return true;
                    }
                    conteudo.append(linha);
                }
            }
            lerArq.close();
            fileReader.close();
            return conteudo.toString().contains(texto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
