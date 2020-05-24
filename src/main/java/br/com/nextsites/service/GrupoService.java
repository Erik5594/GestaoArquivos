package br.com.nextsites.service;

import br.com.nextsites.dto.GrupoDto;
import br.com.nextsites.model.Grupo;
import br.com.nextsites.repository.Grupos;
import br.com.nextsites.util.jpa.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 24/05/2020 - 09:17
 */
public class GrupoService {

    @Inject
    private Grupos grupoDao;

    @Transactional
    public List<GrupoDto> getGrupos(){
        List<GrupoDto> gruposDto = new ArrayList<>();
        List<Grupo> gruposModel = grupoDao.getGrupos();
        if(gruposModel != null && !gruposModel.isEmpty()){
            for(Grupo grupoModel : gruposModel){
                gruposDto.add(new GrupoDto(grupoModel));
            }
        }
        return gruposDto;
    }

    @Transactional
    public GrupoDto getGrupoById(Long id){
        return new GrupoDto(grupoDao.porId(id));
    }
}
