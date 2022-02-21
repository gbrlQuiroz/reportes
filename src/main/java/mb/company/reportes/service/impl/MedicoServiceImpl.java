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
import java.io.FileInputStream;
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

                try {
                    String ruta = filesystemQR + "qrCode-AA9.png";
                    // log.info("--->>>ruta: {}", ruta);
                    // System.out.println("--->>>ruta: " + ruta);
                    
                    // buscar archivo en todo el filesystem
                    InputStream qrArchivo = new FileInputStream(ruta);
                    // solo permite buscar dentro del proyecto
                    // InputStream qrArchivo = getClass().getResourceAsStream("/qr/qrCode-AA9.png");
                    byte[] qrCodificado = Base64.encodeBase64(IOUtils.toByteArray(qrArchivo));
                    String qrFile = IOUtils.toString(qrCodificado, "UTF-8");
                    param.put("imgQR", qrFile);

                    // como queda el parametro en JasperReports
                    // new
                    // java.io.ByteArrayInputStream(org.apache.commons.codec.binary.Base64.decodeBase64(($P{imgQR}).getBytes()));
                } catch (Exception ex) {
                    log.error("Error: {}", ex.getCause());
                    System.out.println("--->>>Error: " + ex.getCause());
                    String img = "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAMAAAD04JH5AAAA5FBMVEXlanf///8AAAAEBATka3gICAjTYW1qMTfndID09PT8/PwUFBQSCAklERPw8PDBWWTh4eH30dV+OkFeKzFVJyxra2seHh4NDQ3o6Oj2y883GRw8PDyJP0e2VF5AHSEdDQ+Ojo7T09NfX1/Dw8OsrKx7e3sxMTFNTU3zub/qhY/wq7InJydyNTv76Or99fakTFXunqeWRU64uLjLXmn87vCZmZk4ODjytryRkZHriZSDg4O0tLStUFpKIiYvFhj52NxYWFjRq6+BZWguIiPJsLPLi5LVdH/rk5xHR0dbUVPgxMeqWGHvPMS2AAAJNElEQVR4nNVbaXviNhAW2IAxBnMnJEBijkC4SSC7JAHSbneTtv///9S2btkYCUz7dD7ZGHtejeaURiDxHxP43wLIXheLxeuC+a8DKHTXtcFDPZVEdFW/7bz2nJOBKAFw1p1xMpxSt/23wkUBmE+1+gHmmLTb1+KlADi13BHuiF7ur+MHkF0/yHGHk/HRjRfAdT9s8FquPn54GNevQsXQiw9AoS+w0B469z2H6ptZ7K77A1E9HqQhRAMw77nRu1rWzYb/87pX4w3kRXIiIgE8sd9MffTC7Gw23TeGPjV++/0HC6EjpY4RAAodVqbrwNBn++Hi0QAc2RmLvpNbnwXgiUpfCyi2uR896iCM9GaeQhgcF8IhAGaNsu8I7sUsLYxQ5oiWFELu7UQAxRc6Cod/NB1FcocQ7sjr/SNRIhxAl4h//MQ/KW2OcvcnYqLhL3xFh4hQAD0c6zQev9l4lGLvkX1D9DcyQIQBeCXDd/jRy7P3qIw/U3dCmEQA6OMXPzjhTT+V2Lv0jjUhF+GUggCw+qe+s7+a38KtLpLSbfStq8MIAgD6oainatInlMEI/pAFcB86b8MThg8JK4L1cyYFoIfVj9Xc2eJU9i4tkT3e/ZpKAOgi+3th1e9U8SOaIwQ7vXEUwDXyP2OW//644zsiAyTVKvh2BIB5i+aflX/p5Okn1EQImmARDQAZQM5hfmuczx+ACfKrNlgEIgMDoAsnS+vGzR+AKkSQ14MIKIAsyuvu45W/TzoK0BkQmAUKAHnAAfNwHxN/NzQh82oBMDoAoIsUkDGA6bn6z9AS2yIAw1AA5kNAAWbn2b9AVWwJQN+HAVjD5zXmkXL0i6Q0zFatNADGKgggC11QnUl9v8XK3/WIRA/BxgwAQDGIqWfiU0BMMDZrz4BTRMAK4PZSCuCTDQdZ8a73AoDv8BmjgaPY+QNQgSJwtQA8zjgAZl10AdPYJ8ClZ+hqJ971iAOAsgCHApBLvlUJisDyBqdPWQBf/oMvyr9xEf5YC5be9ScDoAh/p1WUGaML5GjrM2r713sKANYBdWqcwwvxx77As0SwoQDgKsArNcFLCQAAWCmU/esSBuBAWMXLCwCnJjv/eoMBvIpOKH4fRMhm5gBMEQCYCdI6qHQ5/gDkcUwEKDcBiYImzIBkFLQreeuu2lIEAEulrX+tz3wAb/5PY8J/JecEUaqrLdUAvENfBG+GPoC+kAjIqSD8EArwKqQxSrDxAdwKgVjOC+8wAGRT0nRDnaGvhiBxxavASuozz4Q/8mvSBA0xA2/cOQDQD+cUw8CcArhTA7BkUbsBAbwJXkDOBpYUgKYGwGa10LUD8F3QQTk3zEggqQYAaSFS3VICwHqElENTua+8nw4AuiIb3owS4IM3Ask40KL8LUUAMDedw5vPBIBWSLJBycUQ+3QAFdYOjQQY81YomYsxZphXBJDhvMcKwIQ8q6aDIE0B3CgCKLOOAJQA9EM4G5JzQy5RAFtFAE0OwBD4dbOGBbCX/Qzdl6icBAC/NYJDuVLzgwAbk0cTRQBLDsAn9AspRStkg1HzLAAbpAMYgHRJ3CYAFBMCQQceBSuQLgm3BMC7IgDeCgzBD0gvylYIAFsRwITzAwaAm0OOKoAMAaCYESHoWHN0MPDvn1QBkO0QxWiMJ29O7mE0XKsCwMuvqvkIth+cTesAliV9VQAkI9mpArC4iTMAXBsYqFoBSQgUU0IURVL41gCwMqyr+gESj6uKAOa83B6BCRdRC4quWMcAFLNypL0kgGxA4oE1A/m6EAeD+fG/crTlrBAsAFqk7iulhIA4Ak3VDVicEbjREK1Qobx8Jv0hlBWqBmOoOxqpPxsArRBpBaWMCKDJ3KkKoCyYzh7gFRqUFyusULcmmaXycmJb0NwZwDsVH4qO4DRKw7KEBDDDWyF5gq4hq2aHIN3ctiuqNtAU3PfCA2DmmHAgnZXOoTrfqCkBLM4z5H7oL9PVWDuQXKFq4Q6JvAoC5D/pus7eB4C2ixwVJaA5oUpSCp0HLWUMtFYM7aCj4AuZ0kyhMkLbNtQGFggA3C9J+U0/csukp5XnZdF5NhCAwhXjjqVSAgaAfEqkQwHQ+Omt08EdkxojAilDZIpT+YykLKqgt1IJARShTvvrJHJr9bQukC5MkAYwGUyJAEAi0BxpO7BxcXgj7Y0zogAMkwIowrTkSz4k29AQK9JuwNZEAfj9DHjnFG1drxUCUqs8aT7LssdOkKlj/KViAqAA/XHO00PpGl2FUB7P5A9wI5/snqPN4y9vXi6wZ4YmgFlZRvtmtH8ANZC8XkQE6XzAZFAnAwXgQD30N/Dj3TgHZPOe0UB9JQLAG9j16/g3TpECWIzO4pYetosG7l8mX7JxJ0bvKHYzaxnGLAQA7mNyFTHWnbsWaiBhi6hSIgQAbuTx8sMYN2+x09wx80p7afheMtxLV1Ook4/R811QAZgmDqGbDnczdsy4JsFG/DV2LamUOATAHCAEg2w8PRwtHLTYxTS2lUhsaMwif5S8LcSxgzvH/bls0GZ7WIItnQXc0jx2zleDMs6d2SKe6+IJaWotYBmk1mfGhDRZTOT4852VIW292S/83p+/zuHfIv3drPz5PqrwxmbzA7/5Q3UZlGFExM/t7uqlxHEATG+zQsYjDJ9ULha7vx3sKz3QXd8jJ0ss1dVwj56rZAQ7NmkK6Ws9dL7Aoac78qrr4ekMaa1PVlhvYojyjwCQyNITDi4EBaf0zJzysLjy3QjrLI44Y/LEnN2xJpLp5/uWjj655V7arMK4RJ2yydaYjyXbzaP6aE/umBfuuOHro/CjFtHnjJwv5oNJ7aZ8uGMjPa/kuT9POLxh0y8BIJF4E46ZWe3J0hY0Iv3erOz4vyWr/JR9hopfBoBrkSEn3axdu1rJuFTZtvOpwGOtym+jGKFd5bIAXG0caEEMh8nK8KPXRwc6+6UBuKXj67HDjoRuRJNdhLf1qwFwqXv0wKXHvSxYq36EvdqZU+d+EHHuMl9ZinZqjI6xVz916/T6g7GgEtauWp4HncSmETX3pwLwyfzr59/LZrPcbC7n73aYf9I/hwcNLwYAHu2HnwfyZn0zKsmM/UwAHq1Kw8UnPXJoGJvFqDFVOwMdx9lzc7ZaTVez0w5f/38Pv8dF/wCZgq+8kCSszAAAAABJRU5ErkJggg==";
                    param.put("imgQR", img);    
                }

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
