package br.com.nextsites.converter;

import br.com.nextsites.dto.GrupoDto;
import br.com.nextsites.service.GrupoService;
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
 * Data Criacao: 24/05/2020 - 10:01
 */
@FacesConverter(forClass = GrupoDto.class)
public class GrupoConverter implements Converter {

    @Inject
    private GrupoService grupos;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        GrupoDto retorno = null;

        if (StringUtils.isNotEmpty(value)) {
            retorno = this.grupos.getGrupoById(new Long(value));
        }

        return retorno;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return ((GrupoDto) value).getId().toString();
        }
        return "";
    }
}
