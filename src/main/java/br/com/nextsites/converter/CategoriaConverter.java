package br.com.nextsites.converter;

import br.com.nextsites.dto.CategoriaDto;
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
@FacesConverter(forClass = CategoriaDto.class)
public class CategoriaConverter implements Converter {


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        CategoriaDto retorno = null;

        if (StringUtils.isNotEmpty(value)) {
            retorno = new CategoriaDto();
        }

        return retorno;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null && ((CategoriaDto) value).getNomeCategoria() != null) {
            return ((CategoriaDto) value).getNomeCategoria();
        }
        return "";
    }
}
