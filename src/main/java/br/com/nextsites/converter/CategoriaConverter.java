package br.com.nextsites.converter;

import br.com.nextsites.dto.CategoriaDto;
import br.com.nextsites.service.CategoriaService;
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
@FacesConverter(forClass = CategoriaDto.class, value = "categoriaConv")
public class CategoriaConverter implements Converter {

    @Inject
    private CategoriaService categoriaService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        CategoriaDto retorno = null;

        if (StringUtils.isNotEmpty(value)) {
            retorno = categoriaService.findById(Long.parseLong(value));
        }

        return retorno;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null && ((CategoriaDto) value).getId() != null) {
            return ((CategoriaDto) value).getId().toString();
        }
        return "";
    }
}
