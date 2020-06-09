package br.com.nextsites.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 23/05/2020 - 16:42
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public AppUserDetailsService userDetailsService() {
        return new AppUserDetailsService();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new JsfLoginUrlAuthenticationEntryPoint();
        jsfLoginEntry.setLoginFormUrl("/Login.xhtml");
        jsfLoginEntry.setRedirectStrategy(new JsfRedirectStrategy());

        JsfAccessDeniedHandler jsfDeniedEntry = new JsfAccessDeniedHandler();
        jsfDeniedEntry.setLoginPath("/AcessoNegado.xhtml");
        jsfDeniedEntry.setContextRelative(true);

        http
                .csrf().disable()
                .headers().frameOptions().sameOrigin()
                .and()

                .authorizeRequests()
                    .antMatchers("/Login.xhtml", "/Erro.xhtml", "/RecuperarSenha.xhtml", "/javax.faces.resource/**").permitAll()
                    .antMatchers("/index.xhtml", "/AcessoNegado.xhtml", "/pages/arquivo/DownloadArquivo.xhtml", "/pages/usuario/MinhaConta.xhtml").authenticated()
                    .antMatchers("/pages/arquivo/UploadArquivo.xhtml").hasRole("ADMINISTRADOR")
                .antMatchers("/pages/usuario/CadastroUsuario.xhtml", "/pages/usuario/PesquisaUsuarios.xhtml").hasRole("ADMINISTRADOR")
                .antMatchers("/pages/categoria/CadastroCategoria.xhtml").hasRole("ADMINISTRADOR")
                    .and()

                .formLogin()
                    .loginPage("/Login.xhtml")
                    .failureUrl("/Login.xhtml?invalid=true")
                    .and()

                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .and()

                .exceptionHandling()
                    .accessDeniedPage("/AcessoNegado.xhtml")
                    .authenticationEntryPoint(jsfLoginEntry)
                    .accessDeniedHandler(jsfDeniedEntry);
    }
}
