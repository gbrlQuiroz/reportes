package mb.company.reportes.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mb.company.reportes.model.Medico;
import mb.company.reportes.persistence.MedicoRepository;
import mb.company.reportes.service.MedicoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.InputStream;

import net.sf.jasperreports.engine.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;

@Service
@Transactional(readOnly = true)
@Slf4j
public class MedicoServiceImpl implements MedicoService {

    // private final Logger log = LoggerFactory.getLogger(MedicoServiceImpl.class);

    @Value("${archivosPDF}")
    String filesystemPDF;

    @Value("${archivosQR}")
    String filesystemQR;

    private MedicoRepository MedicoRepository;

    @Autowired
    public void setMedicoRepository(MedicoRepository MedicoRepository) {
        this.MedicoRepository = MedicoRepository;
    }

    @Override
    public String getMedico(Long id) {
        try {
            Optional<Medico> medicoOpt = MedicoRepository.findById(id);

            if (medicoOpt.isPresent()) {
                Medico medicoObj = medicoOpt.get();
                // log.info("--->>>medicoOpt: {}", medicoOpt);
                log.info("--->>>medicoObj: {}", medicoObj);
                // return medicoObj.toString();
                Map<String, Object> param = new HashMap<>();
                param.put("txtNombre", medicoObj.getNombre());
                param.put("txtApPaterno", medicoObj.getApPaterno());
                param.put("txtApMaterno", medicoObj.getApMaterno());
                param.put("txtRfc", medicoObj.getRfc());
                param.put("txtFechaNacimiento", medicoObj.getFechaNacimiento());
                param.put("txtEmail", medicoObj.getEmail());
                param.put("txtNumRegistro", (String) medicoObj.getNumRegistro().toString());

                InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/Medico.jrxml");
                JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
                byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, param, new JREmptyDataSource());

                byte[] codificado = Base64.encodeBase64(byteReporte);
                String pdfFile = IOUtils.toString(codificado, "UTF-8");

                String ruta = filesystemPDF + medicoObj.getRfc() + ".pdf";
                FileOutputStream fos = new FileOutputStream(ruta);
                fos.write(byteReporte);
                fos.close();

                return pdfFile;
            }

            log.error("El ID no existe y no regresa ningun valor");
            return "404";

        } catch (JRException jreE) {
            String textoError;
            if (jreE.getMessage().contains("java.net.MalformedURLException")) {
                textoError = "No existe el archivo jrxml";
            } else {
                textoError = jreE.getMessage();
            }
            log.error("getMedico() - {}", textoError);
            return "409 - " + textoError;
        } catch (Exception ex) {
            log.error("Error: {}", ex.getCause());
        }
        return null;
    }


    @Override
    public byte[] getMedicoV2(Long id) {
        try {
            Optional<Medico> medicoOpt = MedicoRepository.findById(id);

            if (medicoOpt.isPresent()) {
                Medico medicoObj = medicoOpt.get();
                // log.info("--->>>medicoOpt: {}", medicoOpt);
                log.info("--->>>medicoObj: {}", medicoObj);
                // return medicoObj.toString();
                Map<String, Object> param = new HashMap<>();
                param.put("txtNombre", medicoObj.getNombre());
                param.put("txtApPaterno", medicoObj.getApPaterno());
                param.put("txtApMaterno", medicoObj.getApMaterno());
                param.put("txtRfc", medicoObj.getRfc());
                param.put("txtFechaNacimiento", medicoObj.getFechaNacimiento());
                param.put("txtEmail", medicoObj.getEmail());
                param.put("txtNumRegistro", (String) medicoObj.getNumRegistro().toString());

                InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/Medico.jrxml");
                JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
                byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, param, new JREmptyDataSource());

                // byte[] codificado = Base64.encodeBase64(byteReporte);
                // String pdfFile = IOUtils.toString(codificado, "UTF-8");

                String ruta = filesystemPDF + medicoObj.getRfc() + ".pdf";
                FileOutputStream fos = new FileOutputStream(ruta);
                fos.write(byteReporte);
                fos.close();

                // return pdfFile;
                return byteReporte;

            }

            log.error("El ID no existe y no regresa ningun valor");
            return "404".getBytes();

        } catch (JRException jreE) {
            String textoError;
            if (jreE.getMessage().contains("java.net.MalformedURLException")) {
                textoError = "No existe el archivo jrxml";
            } else {
                textoError = jreE.getMessage();
            }
            log.error("getMedico() - {}", textoError);
            return ("409 - " + textoError).getBytes();
        } catch (Exception ex) {
            log.error("Error: {}", ex.getCause());
        }
        return null;
    }
}
