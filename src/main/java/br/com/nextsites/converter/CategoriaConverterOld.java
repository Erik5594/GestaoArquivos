package br.com.nextsites.converter;

import br.com.nextsites.dto.CategoriaOldDto;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 09/06/2020 - 00:32
 */
@FacesConverter(forClass = CategoriaOldDto.class)
public class CategoriaConverterOld implements Converter {


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        CategoriaOldDto retorno = null;

        if (StringUtils.isNotEmpty(value)) {
            retorno = new CategoriaOldDto();
        }

        return retorno;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null && ((CategoriaOldDto) value).getNomeCategoria() != null) {
            return ((CategoriaOldDto) value).getNomeCategoria();
        }
        return "";
    }
}
