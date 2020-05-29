package br.com.nextsites.controller;

import br.com.nextsites.dto.ArquivoDto;
import br.com.nextsites.dto.CategoriaDto;
import br.com.nextsites.dto.PermissaoDto;
import br.com.nextsites.dto.UsuarioDto;
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

    @Getter @Setter
    private String pesquisa;

    @Inject
    private ArquivoService arquivoService;

    @Getter @Setter
    List<ArquivoDto> arquivos;

    @Getter @Setter
    ArquivoDto arquivo;

    @PostConstruct
    public void init(){
        limpar();
        arquivos = arquivoService.getTodosArquivos();
    }

    private void limpar(){
        pesquisa = "";
        arquivo = new ArquivoDto();
        arquivos = new ArrayList<>();
    }
}
