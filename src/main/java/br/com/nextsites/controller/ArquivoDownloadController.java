package br.com.nextsites.controller;

import br.com.nextsites.dto.ArquivoDto;
import br.com.nextsites.dto.CategoriaDto;
import br.com.nextsites.dto.PermissaoDto;
import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.security.Seguranca;
import br.com.nextsites.security.UsuarioSistema;
import br.com.nextsites.service.ArquivoService;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.file.FileUtil;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.file.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 25/05/2020 - 14:10
 */

@Named
@ViewScoped
public class ArquivoDownloadController {

    private static final String DELETADO = "Arquivo deletado com sucesso!";

    @Getter @Setter
    private String pesquisa;

    @Inject
    private ArquivoService arquivoService;

    @Inject
    private Seguranca seguranca;

    @Getter @Setter
    private List<ArquivoDto> arquivos;

    @Getter @Setter
    private ArquivoDto arquivo;

    @Getter @Setter
    private DualListModel<UsuarioDto> usuariosPick;

    @PostConstruct
    public void init(){
        limpar();
        if(seguranca != null){
            if(seguranca.isAdministrador()){
                arquivos = arquivoService.getTodosArquivos(null);
            }else{
                arquivos = arquivoService.getTodosArquivos(seguranca.getIdUsuario());
            }
        }
    }

    private void limpar(){
        pesquisa = "";
        arquivo = new ArquivoDto();
        arquivos = new ArrayList<>();
        usuariosPick = new DualListModel<>();
    }

    public void atualizarPermissoes(){
        List<UsuarioDto> usuarioComPermissao = usuariosPick.getTarget();
        List<PermissaoDto> adcionarPermissoes = new ArrayList<>();

        for(UsuarioDto usuario : usuarioComPermissao){
            adcionarPermissoes.add(new PermissaoDto(usuario.getId(), arquivo.getId()));
        }

        arquivoService.incluirPermissoes(adcionarPermissoes);
        FacesUtil.addInfoMessage(String.format("Permissões para o arquivo %s, categoria %s foram atualizadas com sucesso!",arquivo.getNome().toUpperCase(), arquivo.getDiretorio().toUpperCase()));
        init();
    }

    public void buscarUsuarios(Long idArquivo){
        List<UsuarioDto> usuariosComPermissao = arquivoService.getUsuariosDoArquivo(idArquivo);
        List<UsuarioDto> usuariosSemPermissao = arquivoService.getUsuariosSemPermissao(idArquivo);
        usuariosPick = new DualListModel<>(usuariosSemPermissao,usuariosComPermissao);
    }

    public void deletarArquivo(){
        if(arquivo != null){
            arquivoService.deletarArquivo(arquivo);
            init();
            FacesUtil.addInfoMessage(DELETADO);
        }else{
            FacesUtil.addErrorMessage("Deve ser selecionado um arquivo para exclusão");
        }
    }
}
