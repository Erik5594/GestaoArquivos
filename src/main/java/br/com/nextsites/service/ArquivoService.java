package br.com.nextsites.service;

import br.com.nextsites.dto.ArquivoDto;
import br.com.nextsites.model.Arquivo;
import br.com.nextsites.repository.Arquivos;
import br.com.nextsites.repository.Usuarios;
import br.com.nextsites.util.file.FileUtil;
import br.com.nextsites.util.jpa.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.file.UploadedFile;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

    @Inject
    private Usuarios usuarioDao;

    @Inject
    private Arquivos arquivoDao;

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

    public void salvarVariosDocumentos(List<ArquivoDto> arquivoDtoList){
        for(ArquivoDto arquivoDto : arquivoDtoList){
            arquivoDto.setDataEnvio(new Date());
            salvarDocumento(arquivoDto);
        }
    }

    @Transactional
    public void salvarDocumento(ArquivoDto arquivoDto){
        arquivoDao.salvar(new Arquivo(arquivoDto));
    }
}
