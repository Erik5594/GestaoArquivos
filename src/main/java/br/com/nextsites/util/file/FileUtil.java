package br.com.nextsites.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 25/05/2020 - 21:12
 */

public class FileUtil {

    public static final String SEPARADOR = System.getProperty("file.separator");
    private static final String PRINCIPAL = System.getProperty("user.home")+SEPARADOR+"SGA"+SEPARADOR;

    public static List<File> listaPastasNoDiretorio(String diretorio, String startNome){
        File dir = new File(PRINCIPAL+diretorio);
        List<File> retorno = new ArrayList<>();
        if(dir.exists()){
            File[] files = dir.listFiles();
            for(File arquivo : files){
                if(arquivo.isDirectory() && arquivo.getName().startsWith(startNome)){
                    retorno.add(arquivo);
                }
            }
        }else{
            dir.mkdirs();
        }
        return retorno;
    }
}
