package com.tallerwebi.dominio.servicio.impl;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@Service
@Transactional
public class ServicioPDFImpl {

    ServicioTransacciones servicioTransacciones;

    public ServicioPDFImpl(ServicioTransacciones servicioTransacciones) {
        this.servicioTransacciones = servicioTransacciones;

    }

    public void generarPdf(String filePath, Usuario usuario) {
        Document document = new Document();
        List<Transaccion> transaccionesDelUsuario= servicioTransacciones.obtenerHistorialTransaccionesDeUsuario(usuario.getId());

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();


            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);


            PdfPCell cell1 = new PdfPCell(new com.lowagie.text.Phrase("Tipo de Transaccion"));
            PdfPCell cell2 = new PdfPCell(new com.lowagie.text.Phrase("Fecha de Transaccion"));
            PdfPCell cell3 = new PdfPCell(new com.lowagie.text.Phrase("Criptomoneda"));
            PdfPCell cell4 = new PdfPCell(new com.lowagie.text.Phrase("Precio unitario"));
            PdfPCell cell5 = new PdfPCell(new com.lowagie.text.Phrase("Cantidad"));
            PdfPCell cell6 = new PdfPCell(new com.lowagie.text.Phrase("Total"));

            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);


            for (Transaccion transaccion: transaccionesDelUsuario) {
                table.addCell(transaccion.getTipo().toString());
                table.addCell(transaccion.getFechaDeTransaccion().toString());
                table.addCell(transaccion.getCriptomoneda().getNombreConMayus() + " ( " + transaccion.getCriptomoneda().getSimbolo() + " )"  );
                table.addCell(transaccion.getPrecioAlQueSehizo().toString());
                table.addCell(transaccion.getCantidadDeCripto().toString());
                table.addCell(transaccion.getMontoTotal().toString());
            }


            document.add(table);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
