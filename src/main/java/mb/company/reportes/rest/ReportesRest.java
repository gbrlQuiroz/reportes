package mb.company.reportes.rest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mb.company.reportes.service.ActividadesAvaladasService;

@RestController
@RequestMapping("reportes")
public class ReportesRest {

    private ActividadesAvaladasService actividadesAvaladasService;

    @Autowired
    public void setActividadesAvaladasService(ActividadesAvaladasService actividadesAvaladasService) {
        this.actividadesAvaladasService = actividadesAvaladasService;
    }

    @RequestMapping(value = "ver1/{id}/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getActividadesAvaladas(@PathVariable("id") Long id) {
        return actividadesAvaladasService.getActividadesAvaladas(id);
    }

    @RequestMapping(value = "ver2/{id}/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getActividadesAvaladasV2(@PathVariable("id") Long id, HttpServletResponse res) {
        String respuesta = actividadesAvaladasService.getActividadesAvaladas(id);
        if (respuesta.equals("404")) {
            return new ResponseEntity<String>("No encontrado el ID", null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(respuesta, null, HttpStatus.OK);
    }
}
