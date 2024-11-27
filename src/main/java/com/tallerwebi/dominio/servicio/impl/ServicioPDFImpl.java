package com.tallerwebi.dominio.servicio.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;

import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
public class ServicioPDFImpl {

    ServicioTransacciones servicioTransacciones;

    public ServicioPDFImpl(ServicioTransacciones servicioTransacciones) {
        this.servicioTransacciones = servicioTransacciones;

    }

    public void generarPdf(String filePath, Usuario usuario) {
        Document document = new Document(PageSize.A4.rotate());
        List<Transaccion> transaccionesDelUsuario = servicioTransacciones.obtenerHistorialTransaccionesDeUsuario(usuario.getId(), null, null);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            File imageFile = new File("src/main/webapp/resources/core/img/logoCripto.png");
            if (imageFile.exists()) {
                Image logo = Image.getInstance(imageFile.getAbsolutePath());
                logo.setAlignment(Image.ALIGN_CENTER);
                logo.scaleToFit(50, 50);
                document.add(logo);
            } else {
                System.out.println("La imagen no se encuentra en la ruta especificada.");
                document.add(new Paragraph("Crypto"));
            }

            Chunk crypto = new Chunk("Crypto - " + LocalDate.now().toString());
            Paragraph leftAligned = new Paragraph(crypto);
            leftAligned.setAlignment(Element.ALIGN_CENTER);

            document.add(leftAligned);
            document.add(new Paragraph(""));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            String[] headers = {
                    "Tipo", "Fecha", "Criptomoneda", "Precio Unitario (USD)", "Cantidad", "Total (USD)"
            };
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new com.lowagie.text.Phrase(header, new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.BOLD, new Color(238, 185, 19))));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(new Color(64, 64, 64));
                cell.setPadding(5f);
                table.addCell(cell);
            }

            if (transaccionesDelUsuario.isEmpty()) {
                PdfPCell emptyCell = new PdfPCell(new com.lowagie.text.Phrase("Aún no hay transacciones.", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.NORMAL, Color.BLACK)));
                emptyCell.setColspan(6);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                emptyCell.setPadding(10f);
                emptyCell.setBackgroundColor(new Color(244, 244, 244));
                table.addCell(emptyCell);
            } else {
                for (Transaccion transaccion : transaccionesDelUsuario) {
                    table.addCell(darleEstiloALaCelda(transaccion.getTipo().toString()));
                    table.addCell(darleEstiloALaCelda(transaccion.getFechaDeTransaccion().toString()));

                    String criptoTexto = transaccion.getTipo().equals(TipoTransaccion.INTERCAMBIO) ?
                            transaccion.getCriptomoneda().getNombreConMayus() + " (" + transaccion.getCriptomoneda().getSimbolo() + ") → " +
                                    transaccion.getCriptomoneda2().getNombreConMayus() + " (" + transaccion.getCriptomoneda2().getSimbolo() + ")" :
                            transaccion.getCriptomoneda().getNombreConMayus() + " (" + transaccion.getCriptomoneda().getSimbolo() + ")";
                    table.addCell(darleEstiloALaCelda(criptoTexto));

                    String precioTexto = transaccion.getTipo().equals(TipoTransaccion.INTERCAMBIO) ?
                            transaccion.getPrecioAlQueSehizo() + " → " + transaccion.getPrecioAlQueSehizo2() :
                            transaccion.getPrecioAlQueSehizo().toString();
                    table.addCell(darleEstiloALaCelda(precioTexto));

                    String cantidadTexto;
                    if (transaccion.getTipo().equals(TipoTransaccion.VENTA) || transaccion.getTipo().equals(TipoTransaccion.DEVOLUCION)) {
                        cantidadTexto = "-" + transaccion.getCantidadDeCripto();
                    } else if (transaccion.getTipo().equals(TipoTransaccion.COMPRA)) {
                        cantidadTexto = "+" + transaccion.getCantidadDeCripto();
                    } else {
                        cantidadTexto = "-" + transaccion.getCantidadDeCripto() + " → +" + transaccion.getCantidadDeCripto2();
                    }
                    table.addCell(darleEstiloALaCelda(cantidadTexto));

                    table.addCell(darleEstiloALaCelda(transaccion.getMontoTotal().toString()));
                }
            }

            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private PdfPCell darleEstiloALaCelda(String content) {
        PdfPCell cell = new PdfPCell(new com.lowagie.text.Phrase(content, new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.NORMAL, Color.BLACK)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f); // Padding interno
        cell.setBackgroundColor(new Color(244, 244, 244));
        return cell;
    }
}
