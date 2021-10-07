package mb.company.reportes.rest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mb.company.reportes.service.ActividadesAvaladasService;
import mb.company.reportes.service.MedicoService;

@RestController
@RequestMapping("reportes")
public class ReportesRest {

    private ActividadesAvaladasService actividadesAvaladasService;
    private MedicoService medicoService;

    @Autowired
    public void setActividadesAvaladasService(ActividadesAvaladasService actividadesAvaladasService) {
        this.actividadesAvaladasService = actividadesAvaladasService;
    }

    @Autowired
    public void setMedicoService(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @RequestMapping(value = "actaval/aver1/{id}/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getActividadesAvaladas(@PathVariable("id") Long id) {
        return actividadesAvaladasService.getActividadesAvaladas(id);
    }

    @RequestMapping(value = "actaval/ver2/{id}/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getActividadesAvaladasV2(@PathVariable("id") Long id, HttpServletResponse res) {
        String respuesta = actividadesAvaladasService.getActividadesAvaladas(id);
        if (respuesta.equals("404")) {
            return new ResponseEntity<String>("No encontrado el ID", null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(respuesta, null, HttpStatus.OK);
    }

    @RequestMapping(value = "medico/{id}/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getMedico(@PathVariable("id") Long id, HttpServletResponse res) {
        String respuesta = medicoService.getMedico(id);
        if (respuesta.equals("404")) {
            return new ResponseEntity<String>("No encontrado el ID", null, HttpStatus.NOT_FOUND);
        }
        if (respuesta.contains("409")) {
            return new ResponseEntity<String>("Error: " + respuesta, null, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(respuesta, null, HttpStatus.OK);
    }

    @RequestMapping(value = "medico/ver2/{id}/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody byte[] getMedicoV2(@PathVariable("id") Long id, HttpServletResponse res) {
        byte[] respuesta = medicoService.getMedicoV2(id);
        return respuesta;
    }
}
