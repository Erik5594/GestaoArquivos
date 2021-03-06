package br.com.nextsites.controller;

import br.com.nextsites.dto.*;
import br.com.nextsites.security.Seguranca;
import br.com.nextsites.service.ArquivoService;
import br.com.nextsites.service.CategoriaService;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.event.*;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.file.UploadedFile;

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
    private boolean exibirUploadFiles;

    @Getter @Setter
    private boolean exibirPermissao;

    @Getter @Setter
    private boolean exibirPainelCategoria;

    @Getter @Setter
    private boolean exibirPainelAlterarNome;

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

    @Getter @Setter
    private TreeNode opcoesCategoriaArquivo;

    @Getter @Setter
    private TreeNode selecionadoCategoriaArquivo;

    @Inject
    private CategoriaService categoriaService;

    @Getter @Setter
    private CategoriaDto categoriaSelecionadaExibirArquivos;

    @Getter @Setter
    private CategoriaDto categoriaSelecionadaCarregarArquivos;

    @Getter @Setter
    private CategoriaDto categoriaSelecionadaEdicao;

    private String nomeDiretorio;

    private void carregarArvoreCategoriaRoot(){
        root = new DefaultTreeNode();

        List<CategoriaDto> categorias = buscarCategoriasUsuario();

        for(CategoriaDto categoriaDto : categorias){
            root = categoriaService.retornoArvoreCategoria(root, categoriaDto.getId());
        }
    }

    private void carregarArvoreCategoriaArquivo(){
        opcoesCategoriaArquivo = new DefaultTreeNode();

        List<CategoriaDto> categorias = buscarCategoriasUsuario();

        for(CategoriaDto categoriaDto : categorias){
            opcoesCategoriaArquivo = categoriaService.retornoArvoreCategoria(opcoesCategoriaArquivo, categoriaDto.getId());
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

            carregarArvoreCategoriaRoot();
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

    public void editarCategoria(){
        if(categoriaSelecionadaEdicao != null){
            categoriaService.editarNomeCategoria(categoriaSelecionadaEdicao.getId(), categoriaSelecionadaEdicao.getNomeCategoria());
            FacesUtil.addInfoMessage("Categoria editada com sucesso!");
        }
    }

    public void editarNovosArquivos(){
        exibirPainelPermissao();
    }

    public void salvar(){
        if(validar()){
            salvarArquivos();
            incluirPermissoes();

            limpar();
            buscarArquivos();
            semExibicao();

            FacesUtil.addInfoMessage("Documento(s) salvo(s) com sucesso!");
        }
    }

    private void incluirPermissoes(){
        List<PermissaoDto> adcionarPermissoes = new ArrayList<>();

        for(ArquivoDto arquivoDto : novosArquivos){
            for(UsuarioDto usuario : usuariosComPermissao){
                adcionarPermissoes.add(new PermissaoDto(usuario.getId(), arquivoDto.getId()));
            }
        }

        arquivoService.incluirPermissoes(adcionarPermissoes);
    }

    private void salvarArquivos(){
        if(novosArquivos != null && !novosArquivos.isEmpty()){
            if(arquivos == null){
                arquivos = new ArrayList<>();
            }
            List<ArquivoDto> arquivosSalvos = new ArrayList<>();
            for(ArquivoDto arquivo : novosArquivos){
                arquivo = arquivoService.salvarDocumento(arquivo);
                arquivosSalvos.add(arquivo);
                arquivos.add(arquivo);
                try {
                    arquivoService.gravarArquivoEmDisco(arquivo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            novosArquivos = arquivosSalvos;
        }
    }

    private boolean validar(){
        boolean retorno = true;
        if(novosArquivos == null || novosArquivos.isEmpty()){
            retorno = false;
            FacesUtil.addErrorMessage("Nenhum arquivo carregado!");
        }
        return retorno;
    }

    public void limpar(){
        categoriaSelecionadaCarregarArquivos = null;
        novosArquivos = new ArrayList<>();
        usuariosComPermissao = new ArrayList<>();
        selecionadoCategoriaArquivo = null;
        carregarArvoreCategoriaArquivo();
        exibirPainelCategoria();
    }

    public void carregarArquivo(FileUploadEvent event) {
        if(categoriaSelecionadaCarregarArquivos != null){
            addArquivo(event.getFile());
        }
    }

    private synchronized void addArquivo(UploadedFile file){
        if(novosArquivos == null){
            novosArquivos = new ArrayList<>();
        }
        ArquivoDto arquivoDto = new ArquivoDto();
        arquivoDto.setNome(file.getFileName());
        arquivoDto.setConteudo(file.getContent());
        arquivoDto.setDataEnvio(new Date());
        arquivoDto.setCategoriaDto(categoriaSelecionadaCarregarArquivos);

        novosArquivos.add(arquivoDto);
    }

    public void selecionarCategoria(NodeSelectEvent event) {
        categoriaSelecionadaCarregarArquivos = (CategoriaDto) event.getTreeNode().getData();
        abrirTreeNode(event.getTreeNode());
    }

    public void onNodeSelect(NodeSelectEvent event) {
        categoriaSelecionadaExibirArquivos = (CategoriaDto) event.getTreeNode().getData();
        this.nomeDiretorio = extrairCaminho(event.getTreeNode(), null);
        buscarArquivos();
    }

    private String extrairCaminho(TreeNode treeNode, String caminho){
        String separador = "/";
        if(caminho == null){
            caminho = "";
        }
        if(treeNode != null) {
            treeNode.setExpanded(true);
            if (treeNode.getData() != null) {
                caminho = separador + ((CategoriaDto) treeNode.getData()).getNomeCategoria() + caminho;
                if (treeNode.getParent() != null) {
                    caminho = extrairCaminho(treeNode.getParent(), caminho);
                }
            }
        }
        return caminho;
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

        return arquivoDtos != null && !arquivoDtos.isEmpty() || (idUsuario == null && categorias != null && !categorias.isEmpty());
    }

    public String getNomeDiretorio(){
        if(StringUtils.isBlank(this.nomeDiretorio) || "/".equals(this.nomeDiretorio)){
            return "Arquivos";
        }
        return this.nomeDiretorio;
    }

    public void selecionarCategoriaArquivo(){
        if(categoriaSelecionadaCarregarArquivos != null){
            exibirPainelUpload();
        }else{
            FacesUtil.addErrorMessage("Deve ser selecionado uma categoria!");
        }
    }

    public void conferir(){
        if(novosArquivos != null && !novosArquivos.isEmpty()){
            exibirPainelNome();
        } else {
            FacesUtil.addErrorMessage("Deve ser carregado pelo menos 1 arquivo.");
        }
    }

    private void abrirTreeNode(TreeNode treeNode){
        if(treeNode != null){
            treeNode.setExpanded(true);
            if(treeNode.getParent() != null){
                abrirTreeNode(treeNode.getParent());
            }
        }
    }

    private void exibirPainelCategoria(){
        exibirPainelCategoria = true;
        exibirUploadFiles = false;
        exibirPainelAlterarNome = false;
        exibirPermissao = false;
    }

    private void exibirPainelUpload(){
        exibirPainelCategoria = false;
        exibirUploadFiles = true;
        exibirPainelAlterarNome = false;
        exibirPermissao = false;
    }

    private void exibirPainelNome(){
        exibirPainelCategoria = false;
        exibirUploadFiles = false;
        exibirPainelAlterarNome = true;
        exibirPermissao = false;
    }

    private void exibirPainelPermissao(){
        exibirPainelCategoria = false;
        exibirUploadFiles = false;
        exibirPainelAlterarNome = false;
        exibirPermissao = true;
    }

    private void semExibicao(){
        exibirPainelCategoria = false;
        exibirUploadFiles = false;
        exibirPainelAlterarNome = false;
        exibirPermissao = false;
    }

    public void editarNode() {
        categoriaSelecionadaEdicao = (CategoriaDto) selectedNode.getData();
    }

    public void deleteNode() {
        categoriaSelecionadaEdicao = (CategoriaDto)selectedNode.getData();
    }

    public void deletarCategoria(){
        if(categoriaSelecionadaEdicao != null){
            if(!categoriaService.deletar(categoriaSelecionadaEdicao.getId())){
                FacesUtil.addErrorMessage("Não é possível deletar essa categoria. Existe arquivos nela ou nas suas sub-categorias.");
            }
            carregarArvoreCategoriaRoot();
        }
    }

}
