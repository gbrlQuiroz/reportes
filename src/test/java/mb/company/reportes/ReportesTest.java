package mb.company.reportes;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class ReportesTest extends ReportesTestConfiguration{
    
    @Test
    public void getMedicosPDF() throws Exception {
       String idMedico = "36";
       mockMvc.perform(MockMvcRequestBuilders.get("/reportes/medico/" + idMedico + "/")
          .contentType(JSON))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andDo(MockMvcResultHandlers.print());
       System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>OK(200)");
 
 
    }

    @Test
    public void getMedicosPDFV2() throws Exception {
       String idMedico = "36";
       mockMvc.perform(MockMvcRequestBuilders.get("/reportes/medico/ver2/" + idMedico + "/")
          .contentType(JSON))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andDo(MockMvcResultHandlers.print());
       System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>OK(200)");
 
 
    }

}
