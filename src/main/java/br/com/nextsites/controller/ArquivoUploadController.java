package br.com.nextsites.controller;

import br.com.nextsites.dto.*;
import br.com.nextsites.security.Seguranca;
import br.com.nextsites.service.ArquivoService;
import br.com.nextsites.service.CategoriaService;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.file.FileUtil;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.*;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;
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
public class ArquivoUploadController {

    private static final String DELETADO = "Arquivo deletado com sucesso!";

    @Getter @Setter
    private List<CategoriaDto> categorias;

    @Getter @Setter
    private List<UsuarioDto> usuarios;

    @Getter @Setter
    private List<UsuarioDto> usuariosComPermissao;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private ArquivoService arquivoService;

    @Inject
    private Seguranca seguranca;

    @Getter @Setter
    private boolean exibirUploadFiles = false;

    @Getter @Setter
    private boolean exibirPermissao = false;

    @Getter @Setter
    private boolean continuar = false;

    @Getter @Setter
    private String pesquisa;

    @Getter @Setter
    private List<ArquivoDto> arquivos;

    @Getter @Setter
    private ArquivoDto arquivoSelecionado;

    @Getter @Setter
    private List<ArquivoDto> novosArquivos = new ArrayList<>();

    @Getter @Setter
    private TreeNode root;

    @Getter @Setter
    private TreeNode selectedNode;

    @Inject
    private CategoriaService categoriaService;

    @Getter @Setter
    private CategoriaDto categoriaSelecionadaExibirArquivos;

    @Getter @Setter
    private CategoriaDto categoriaSelecionadaCarregarArquivos;

    private void carregarArvoreCategoria(){
        root = new DefaultTreeNode();

        List<CategoriaDto> categorias = buscarCategoriasUsuario();

        for(CategoriaDto categoriaDto : categorias){
            root = categoriaService.retornoArvoreCategoria(root, categoriaDto.getId());
        }
    }

    private List<CategoriaDto> buscarCategoriasUsuario(){
        List<CategoriaDto> categorias = new ArrayList<>();
        if(seguranca.isAdministrador()){
            categorias = categoriaService.buscarTodas();
        }else{
            List<ArquivoDto> arquivos = arquivoService.getTodosArquivos(seguranca.getIdUsuario());
            if(arquivos != null){
                for(ArquivoDto arq : arquivos){
                    categorias.add(arq.getCategoriaDto());
                }
            }
        }
        if(categorias == null){
            categorias = new ArrayList<>();
        }
        return categorias;
    }

    private void inicializarListaClientes(){
        usuarios = usuarioService.getListaClientes();
    }

    private void inicializarListaCategorias(){
        categorias = categoriaService.buscarTodas();
    }

    public void inicializar() {
        if (FacesUtil.isNotPostback()) {
            categorias = new ArrayList<>();
            usuarios = new ArrayList<>();

            carregarArvoreCategoria();
            inicializarListaClientes();
            inicializarListaCategorias();
            buscarArquivos();
        }
    }

    public void salvarPermissao(){
        if(arquivoSelecionado != null){
            List<PermissaoDto> adcionarPermissoes = new ArrayList<>();
            for(UsuarioDto usuario : usuariosComPermissao){
                adcionarPermissoes.add(new PermissaoDto(usuario.getId(), arquivoSelecionado.getId()));
            }
            arquivoService.incluirPermissoes(adcionarPermissoes);
            FacesUtil.addInfoMessage("Permissões salvas com sucesso!");
        }

    }

    public void buscarUsuarios(Long idArquivo){
        usuariosComPermissao = arquivoService.getUsuariosDoArquivo(idArquivo);
    }

    public void editarArquivo(){
        if(arquivoSelecionado != null){
            arquivoService.editarDocumento(arquivoSelecionado);
            FacesUtil.addInfoMessage("Nome editado com sucesso!");
        }
    }

    public void salvar(){
        if(validar()){
            List<PermissaoDto> adcionarPermissoes = new ArrayList<>();

            for(ArquivoDto arquivoDto : novosArquivos){
                for(UsuarioDto usuario : usuariosComPermissao){
                    adcionarPermissoes.add(new PermissaoDto(usuario.getId(), arquivoDto.getId()));
                }
            }

            arquivoService.incluirPermissoes(adcionarPermissoes);

            limpar();
            buscarArquivos();
            exibirUploadFiles = false;

            FacesUtil.addInfoMessage("Documento salvo com sucesso!");
        }
    }

    private boolean validar(){
        boolean retorno = true;
        if(novosArquivos == null || novosArquivos.isEmpty()){
            retorno = false;
            FacesUtil.addErrorMessage("Nenhum arquivo carregado!");
        }
        if((usuariosComPermissao == null || usuariosComPermissao.isEmpty()) && !continuar){
            retorno = false;
            continuar = true;
            FacesUtil.addErrorMessage("Nenhum usuário foi selecionado para ter acesso aos arquivos carregados. Caso deseje continuar basta clicar em 'Salvar' novamente!");
        }
        return retorno;
    }

    public void limpar(){
        categoriaSelecionadaCarregarArquivos = null;
        novosArquivos = new ArrayList<>();
        usuariosComPermissao = new ArrayList<>();

        exibirUploadFiles = true;
        exibirPermissao = false;
        continuar = false;
    }

    public void carregarArquivo(FileUploadEvent event) {
        try {
            if(categoriaSelecionadaCarregarArquivos != null){
                Long idArquivo = addArquivo(event.getFile());
                arquivoService.gravarArquivoEmDisco(idArquivo, event.getFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gerenciarPermissoes(){
        if(novosArquivos != null && !novosArquivos.isEmpty()){
            exibirUploadFiles = false;
            exibirPermissao = true;
        } else {
            FacesUtil.addErrorMessage("Deve ser carregado pelo menos 1 arquivo.");
        }
    }

    private synchronized Long addArquivo(UploadedFile file){
        if(arquivos == null){
            arquivos = new ArrayList<>();
        }

        if(novosArquivos == null){
            novosArquivos = new ArrayList<>();
        }
        ArquivoDto arquivoDto = new ArquivoDto();
        arquivoDto.setNome(file.getFileName());
        arquivoDto.setConteudo(file.getContent());
        arquivoDto.setDataEnvio(new Date());
        arquivoDto.setCategoriaDto(categoriaSelecionadaCarregarArquivos);
        arquivoDto = arquivoService.salvarDocumento(arquivoDto);

        arquivos.add(arquivoDto);
        novosArquivos.add(arquivoDto);

        return arquivoDto.getId();
    }

    public void onNodeSelect(NodeSelectEvent event) {
        categoriaSelecionadaExibirArquivos = (CategoriaDto) event.getTreeNode().getData();
        event.getTreeNode().setExpanded(true);
        buscarArquivos();
    }

    public boolean categoriaSelecionada(){
        return categoriaSelecionadaCarregarArquivos != null;
    }


    private void buscarArquivos(){
        Long idCategoria = categoriaSelecionadaExibirArquivos == null ? null:categoriaSelecionadaExibirArquivos.getId();

        if(seguranca.isAdministrador()){
            arquivos = arquivoService.getTodosArquivos(null, idCategoria);
        }else{
            arquivos = arquivoService.getTodosArquivos(seguranca.getIdUsuario(), idCategoria);
        }
    }

    public void deletarArquivo(){
        if(arquivoSelecionado != null){
            arquivoService.deletarArquivo(arquivoSelecionado);
            buscarArquivos();
            FacesUtil.addInfoMessage(DELETADO);
        }else{
            FacesUtil.addErrorMessage("Deve ser selecionado um arquivo para exclusão");
        }
    }

    public void pesquisar(){
        categoriaSelecionadaExibirArquivos = null;
        if(StringUtils.isNotBlank(pesquisa) && pesquisa.length() > 2) {
            Long idUsuario = null;
            if (!seguranca.isAdministrador()) {
                idUsuario = seguranca.getIdUsuario();
            }
            arquivos = arquivoService.pesquisarGeral(pesquisa, idUsuario);
            if(arquivos == null || arquivos.isEmpty()){
               FacesUtil.addWarnMessage("Nenhum arquivo encontrado com os parâmetros da pesquisa!");
            }
        }else{
            buscarArquivos();
            FacesUtil.addErrorMessage("É obrigatório digitar pelo menos 3 caracteres para realizar a pesquisa!");
        }
    }

    public boolean isExibirPainelDocumento(){
        Long idUsuario = seguranca.getIdUsuario();
        if(seguranca.isAdministrador()){
            idUsuario = null;
        }
        List<ArquivoDto> arquivoDtos = arquivoService.getTodosArquivos(idUsuario);
        return arquivoDtos != null && !arquivoDtos.isEmpty();
    }
}
