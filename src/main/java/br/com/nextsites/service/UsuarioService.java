package br.com.nextsites.service;

import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.model.Usuario;
import br.com.nextsites.repository.Usuarios;
import br.com.nextsites.util.Consts;
import br.com.nextsites.util.jpa.Transactional;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 24/05/2020 - 09:17
 */
public class UsuarioService {

    @Inject
    private Usuarios usuarioDao;

    @Transactional
    public UsuarioDto getUsuarioPorEmail(String email){
        return new UsuarioDto(usuarioDao.porEmail(email));
    }

    @Transactional
    public UsuarioDto getUsuarioById(Long id){
        return new UsuarioDto(usuarioDao.porId(id));
    }

    public List<String> getValidar(UsuarioDto usuario, String titulo){
        List<String> retornoList = new ArrayList<>();
        if(usuario != null){
            if(StringUtils.isBlank(usuario.getEmail())){
                retornoList.add(String.format(titulo, String.format(Consts.CAMPO_OBRIGATORIO, "E-mail")));
            }else if(!usuario.getEmail().contains("@")){
                retornoList.add(String.format(titulo, String.format(Consts.CAMPO_INVALIDO, "E-mail")));
            }

            if(StringUtils.isBlank(usuario.getNome())){
                retornoList.add(String.format(titulo, String.format(Consts.CAMPO_OBRIGATORIO, "Nome")));
            }

            if(usuario.getNivel() == null || usuario.getNivel().getId() == null  || usuario.getNivel().getId() <= 0l){
                retornoList.add(String.format(titulo, String.format(Consts.CAMPO_OBRIGATORIO, "Nível")));
            }

            if(StringUtils.isBlank(usuario.getSenha())){
                retornoList.add(String.format(titulo, String.format(Consts.CAMPO_OBRIGATORIO, "Senha")));
            }

        }else{
            retornoList.add(String.format(titulo, "Os dados do usuário devem ser preenchidos"));
        }
        return retornoList;
    }

    @Transactional
    public void salvar(UsuarioDto usuarioDto){
        usuarioDao.salvar(new Usuario(usuarioDto));
    }

    @Transactional
    public List<UsuarioDto> getListaClientes(){
        List<UsuarioDto> usuariosDto = new ArrayList<>();
        List<Usuario> usuarios = usuarioDao.listarClientes();
        for(Usuario usuario: usuarios){
            usuariosDto.add(new UsuarioDto(usuario));
        }
        return usuariosDto;
    }

    @Transactional
    public List<UsuarioDto> getListaUsuarios(){
        List<UsuarioDto> usuariosDto = new ArrayList<>();
        List<Usuario> usuarios = usuarioDao.listarUsuarios();
        for(Usuario usuario: usuarios){
            usuariosDto.add(new UsuarioDto(usuario));
        }
        return usuariosDto;
    }
}
