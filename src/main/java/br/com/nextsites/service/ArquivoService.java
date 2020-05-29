package br.com.nextsites.service;

import br.com.nextsites.dto.ArquivoDto;
import br.com.nextsites.dto.PermissaoDto;
import br.com.nextsites.dto.UsuarioDto;
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
        fileUtil.deletarArquivo(arquivo);
    }

    public void incluirPermissoes(List<PermissaoDto> permissoes){
        for(PermissaoDto permissao : permissoes){
            arquivoDao.incluirPermissao(permissao.getIdArquivo(), permissao.getIdUsuario());
        }
    }

    public void removerPermissoes(List<PermissaoDto> permissoes){
        for(PermissaoDto permissao : permissoes){
            arquivoDao.removerPermissao(permissao.getIdArquivo(), permissao.getIdUsuario());
        }
    }

    public ArquivoDto salvarDocumento(ArquivoDto arquivoDto){
        return new ArquivoDto(arquivoDao.salvar(new Arquivo(arquivoDto)));
    }

    public List<ArquivoDto> getTodosArquivos(){
        List<Arquivo> arquivos = arquivoDao.getArquivos();
        List<ArquivoDto> arquivosDto = null;
        if(arquivos != null){
            arquivosDto = new ArrayList<>();
            for(Arquivo arquivo : arquivos){
                arquivosDto.add(new ArquivoDto(arquivo));
            }
        }
        return arquivosDto;
    }
}
