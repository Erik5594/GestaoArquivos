package br.com.nextsites.service;

import br.com.nextsites.dto.ArquivoDto;
import br.com.nextsites.dto.PermissaoDto;
import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.model.Arquivo;
import br.com.nextsites.model.Usuario;
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

    @Inject
    private UsuarioService usuarioService;

    public List<String> nomePastas(String diretorio, String startNome){
        List<String> pastas = new ArrayList<>();
        String start = StringUtils.isBlank(startNome) ? "":startNome;
        List<File> arquivos = FileUtil.listaPastasNoDiretorio(diretorio, start);
        for(File file : arquivos){
            pastas.add(file.getName());
        }
        return pastas;
    }

    public void gravarArquivoEmDisco(Long idArquivo, UploadedFile file) throws IOException {
        if(idArquivo == null){
            throw new NegocioException("Id do Arquivo está null");
        }

        if(arquivoDao.porId(idArquivo) == null){
            throw new NegocioException("Arquivo não foi gravado no banco de dados.");
        }

        fileUtil.gravarArquivo(idArquivo.toString()+".pdf", file.getContent());
        String conteudo = fileUtil.getConteudo(idArquivo.toString()+".pdf");

        fileUtil.gravarArquivoTxt(idArquivo.toString() + ".txt", conteudo);

        fileUtil.deletarArquivo(idArquivo.toString()+".pdf");
    }

    public void incluirPermissoes(List<PermissaoDto> permissoes){
        Long idArquivo = 0l;
        for(PermissaoDto permissao : permissoes){

            if(!permissao.getIdArquivo().equals(idArquivo)){
                removerPermissoes(permissao.getIdArquivo());
                idArquivo = permissao.getIdArquivo();
            }

            arquivoDao.incluirPermissao(permissao.getIdArquivo(), permissao.getIdUsuario());
        }
    }

    private void removerPermissoes(Long idArquivo){
        arquivoDao.removerTodasPermissao(idArquivo);
    }

    public ArquivoDto salvarDocumento(ArquivoDto arquivoDto){
        return new ArquivoDto(arquivoDao.salvar(new Arquivo(arquivoDto)));
    }

    public ArquivoDto editarDocumento(ArquivoDto arquivoDto){
        return new ArquivoDto(arquivoDao.editar(new Arquivo(arquivoDto)));
    }

    public List<ArquivoDto> getTodosArquivos(Long idUsuario){
        return getTodosArquivos(idUsuario, null);
    }

    public List<ArquivoDto> getTodosArquivos(Long idUsuario, Long idCategoria){
        if(idUsuario != null && idUsuario > 0l){
            if(idCategoria != null && idCategoria > 0l){
                return converterListArquivo(arquivoDao.getArquivosCategoria(idUsuario, idCategoria));
            }else{
                return converterListArquivo(arquivoDao.getArquivos(idUsuario));
            }
        }else{
            if(idCategoria != null && idCategoria > 0l){
                return converterListArquivo(arquivoDao.getArquivosCategoria(idCategoria));
            }else{
                return converterListArquivo(arquivoDao.getArquivos());
            }
        }
    }

    public List<UsuarioDto> getUsuariosDoArquivo(Long idArquivo){
        List<Usuario> usuarios = arquivoDao.getUsuariosComAcesso(idArquivo);
        return usuarioService.converterListUsuario(usuarios);
    }

    public List<UsuarioDto> getUsuariosSemPermissao(Long idArquivo){
        List<Usuario> usuarios = arquivoDao.getUsuariosSemPermissao(idArquivo);
        return usuarioService.converterListUsuario(usuarios);
    }



    public void deletarArquivo(ArquivoDto arquivoDto){
        String arquivo = arquivoDto.getId()+".txt";
        fileUtil.deletarArquivo(arquivo);
        arquivoDao.remover(new Arquivo(arquivoDto));
    }

    public List<ArquivoDto> pesquisarGeral(String textoPesquisado, Long idUsuario){
        List<ArquivoDto> todosArquivos;
        List<ArquivoDto> arquivosEncontrados = new ArrayList<>();

        if(idUsuario != null && idUsuario > 0l){
            todosArquivos = converterListArquivo(arquivoDao.getArquivos(idUsuario));
        }else{
            todosArquivos = converterListArquivo(arquivoDao.getArquivos());
        }

        List<ArquivoDto> todosArquivosNome = pesquisarNome(textoPesquisado, idUsuario);
        todosArquivos.removeAll(todosArquivosNome);
        arquivosEncontrados.addAll(todosArquivosNome);

        List<ArquivoDto> todosArquivosConteudo = pesquisarConteudo(todosArquivos, textoPesquisado);
        if(arquivosEncontrados.containsAll(todosArquivosConteudo)){
            for(ArquivoDto arquivo : todosArquivosConteudo){
                if(!arquivosEncontrados.contains(arquivo)){
                    arquivosEncontrados.add(arquivo);
                }
            }
        }else{
            arquivosEncontrados.addAll(todosArquivosConteudo);
        }
        return arquivosEncontrados;
    }

    public List<ArquivoDto> pesquisarNome(String textoPesquisado, Long idUsuario){
        return converterListArquivo(arquivoDao.porNome(textoPesquisado, idUsuario));
    }

    public List<ArquivoDto> pesquisarConteudo(List<ArquivoDto> arquivosQueDevemSerPesquisados, String texto){
        List<ArquivoDto> contemConteudo = new ArrayList<>();
        for(ArquivoDto arquivoDto : arquivosQueDevemSerPesquisados){
            String diretorio = arquivoDto.getId()+".txt";
            if(fileUtil.contemTexto(diretorio, texto.toUpperCase())){
                contemConteudo.add(arquivoDto);
            }
        }
        return contemConteudo;
    }

    private List<ArquivoDto> converterListArquivo(List<Arquivo> arquivos){
        List<ArquivoDto> arquivosDto = null;
        if(arquivos!= null){
            arquivosDto = new ArrayList<>();
            for(Arquivo arquivo : arquivos){
                arquivosDto.add(new ArquivoDto(arquivo));
            }
        }
        return arquivosDto;
    }
}
