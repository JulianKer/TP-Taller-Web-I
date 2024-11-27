package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.impl.ServicioPDFImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@RestController
public class ControladorPDF {
        private final ServicioPDFImpl pdfService;
        @Autowired
        public ControladorPDF(ServicioPDFImpl pdfService) {
            this.pdfService = pdfService;
        }

        @GetMapping("/descargar-pdf")
        public ResponseEntity<InputStreamResource> descargarPdf(HttpServletRequest request) throws IOException {

            String filePath = System.getProperty("java.io.tmpdir") + "Resumen_" + LocalDate.now().toString() + "_Crypto.pdf";
            pdfService.generarPdf(filePath, (Usuario)request.getSession().getAttribute("usuario"));

            File pdfFile = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Resumen_" + LocalDate.now().toString() + "_Crypto.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfFile.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }
  }




