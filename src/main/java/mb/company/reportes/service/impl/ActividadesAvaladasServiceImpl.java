package mb.company.reportes.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mb.company.reportes.model.ActividadesAvaladas;
import mb.company.reportes.persistence.ActividadesAvaladasRepository;
import mb.company.reportes.service.ActividadesAvaladasService;
// import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Transactional(readOnly = true)
// @Slf4j
public class ActividadesAvaladasServiceImpl implements ActividadesAvaladasService {

   private final Logger log = LoggerFactory.getLogger(ActividadesAvaladasServiceImpl.class);


    private ActividadesAvaladasRepository actividadesAvaladasRepository;

    @Autowired
    public void setActividadesAvaladasRepository(ActividadesAvaladasRepository actividadesAvaladasRepository) {
        this.actividadesAvaladasRepository = actividadesAvaladasRepository;
    }

    @Override
    public String getActividadesAvaladas(Long id){
        try{
            Optional<ActividadesAvaladas> actiAval =  actividadesAvaladasRepository.findById(id);
            
            if(actiAval.isPresent()){
                ActividadesAvaladas putaMadre = actiAval.get();
                log.info("--->>>actiAval: {}",actiAval);
                log.info("--->>>actiAval: {}",putaMadre);
                return putaMadre.toString();
            }
            
            log.error("El ID no existe y no regresa ningun valor");
            return "404";

        } catch(Exception ex){
            log.error("Error: {}", ex.getCause());
        }
        return null;
    } 

}
