package br.com.nextsites.service;

import br.com.nextsites.util.file.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.file.UploadedFile;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 25/05/2020 - 21:11
 */
public class ArquivoService {

    @Inject
    private FileUtil fileUtil;

    public List<String> nomePastas(String diretorio, String startNome){
        List<String> pastas = new ArrayList<>();
        String start = StringUtils.isBlank(startNome) ? "":startNome;
        List<File> arquivos = FileUtil.listaPastasNoDiretorio(diretorio, start);
        for(File file : arquivos){
            pastas.add(file.getName());
        }
        return pastas;
    }

    public void gravarArquivo(String diretorio, UploadedFile file) throws IOException {
        if(StringUtils.isBlank(diretorio)){
            diretorio = "";
        }
        String arquivo = diretorio + file.getFileName();
        fileUtil.gravarArquivo(arquivo, file.getContent());
        String conteudo = fileUtil.getConteudo(arquivo);
        fileUtil.gravarArquivoTxt(arquivo+".txt", conteudo);
    }
}
