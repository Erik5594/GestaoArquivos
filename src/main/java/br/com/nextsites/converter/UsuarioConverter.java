package br.com.nextsites.converter;

import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.service.UsuarioService;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 24/05/2020 - 21:54
 */
@FacesConverter(forClass = UsuarioDto.class)
public class UsuarioConverter implements Converter {

    @Inject
    private UsuarioService usuarioService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        UsuarioDto retorno = null;

        if (StringUtils.isNotEmpty(value)) {
            retorno = this.usuarioService.getUsuarioById(new Long(value));
        }

        return retorno;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return ((UsuarioDto) value).getId().toString();
        }
        return "";
    }
}
