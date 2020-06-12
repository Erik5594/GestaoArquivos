package br.com.nextsites.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.nextsites.model.Usuario;
import br.com.nextsites.repository.Usuarios;
import br.com.nextsites.util.cdi.CDIServiceLocator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 23/05/2020 - 16:44
 */
public class AppUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuarios usuarios = CDIServiceLocator.getBean(Usuarios.class);
        Usuario usuario = usuarios.porEmail(email);

        UsuarioSistema user;

        if (usuario != null) {
            user = new UsuarioSistema(usuario, getGrupos(usuario));
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }

        return user;
    }

    private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getNivel().getNome().toUpperCase()));

        return authorities;
    }
}
