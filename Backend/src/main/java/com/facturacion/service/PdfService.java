package com.facturacion.service;

import com.facturacion.model.DetalleFactura;
import com.facturacion.model.Factura;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

//@Service
public class PdfService {

    public byte[] generarFacturaPdf(Factura factura) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // --- HEADER ---
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[] { 1, 2 });

        // Logo
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        try {
            // Load logo from resources
            URL imageUrl = getClass().getResource("/AJM.jpeg");
            if (imageUrl != null) {
                Image logo = Image.getInstance(imageUrl);
                logo.scaleToFit(100, 100);
                logoCell.addElement(logo);
            } else {
                logoCell.addElement(new Phrase("AJM Logo", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            }
        } catch (Exception e) {
            logoCell.addElement(new Phrase("AJM Logo"));
        }
        headerTable.addCell(logoCell);

        // Title and Info
        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Paragraph title = new Paragraph("FACTURA", titleFont);
        title.setAlignment(Element.ALIGN_RIGHT);
        titleCell.addElement(title);

        headerTable.addCell(titleCell);
        document.add(headerTable);

        addEmptyLine(document, 1);

        // --- INVOICE INFO ---
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        document.add(new Paragraph(
                "Factura nº: " + (factura.getId().length() > 8 ? factura.getId().substring(0, 8) : factura.getId()),
                normalFont));

        String fechaStr = factura.getFecha() != null
                ? factura.getFecha().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"))
                : "N/A";
        document.add(new Paragraph("Fecha: " + fechaStr, normalFont));

        String clienteNombre = factura.getCliente() != null ? factura.getCliente().getNombre() : "Consumidor Final";
        document.add(new Paragraph("Cliente: " + clienteNombre, normalFont));

        addEmptyLine(document, 2);

        // --- PRODUCT TABLE ---
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 4, 1, 1.5f, 1.5f });

        // Table Header
        addTableHeader(table, "Descripción");
        addTableHeader(table, "Cantidad");
        addTableHeader(table, "Precio");
        addTableHeader(table, "Total");

        // Table Rows
        for (DetalleFactura detalle : factura.getDetalles()) {
            table.addCell(createCell(detalle.getProducto().getNombre(), Element.ALIGN_LEFT));
            table.addCell(createCell(detalle.getCantidad().toString(), Element.ALIGN_CENTER));
            table.addCell(createCell(String.format("$ %.2f", detalle.getPrecioUnitario()), Element.ALIGN_RIGHT));
            table.addCell(createCell(String.format("$ %.2f", detalle.getSubtotal()), Element.ALIGN_RIGHT));
        }
        document.add(table);

        addEmptyLine(document, 1);

        // --- TOTALS ---
        PdfPTable totalsTable = new PdfPTable(2);
        totalsTable.setWidthPercentage(100);
        totalsTable.setWidths(new float[] { 4, 2 }); // Align right

        addTotalRow(totalsTable, "Subtotal", factura.getSubtotal());
        addTotalRow(totalsTable, "Impuestos (15%)", factura.getIva());

        // Grand Total Big
        PdfPCell labelCell = new PdfPCell(new Phrase("TOTAL", boldFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setPaddingRight(10);
        totalsTable.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(String.format("$ %.2f", factura.getTotal()), boldFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalsTable.addCell(valueCell);

        document.add(totalsTable);

        addEmptyLine(document, 2);

        // Draw Line
        // org.springframework.core.io.Resource resource = new
        // org.springframework.core.io.ClassPathResource("AJM.jpeg");

        // --- FOOTER INFO (Contact / Payment) ---
        // Using a table for side-by-side layout
        PdfPTable footerTable = new PdfPTable(2);
        footerTable.setWidthPercentage(100);
        footerTable.setSpacingBefore(20f);

        // Contact Info
        PdfPCell contactCell = new PdfPCell();
        contactCell.setBorder(Rectangle.TOP);
        contactCell.setPaddingTop(10);
        contactCell.addElement(new Paragraph("Contacto:", boldFont));
        contactCell.addElement(new Paragraph("(55) 1234 - 5678", normalFont));
        contactCell.addElement(new Paragraph("hola@sitioincreible.com", normalFont));
        contactCell.addElement(new Paragraph("www.sitioincreible.com", normalFont));
        footerTable.addCell(contactCell);

        // Payment Info
        PdfPCell paymentCell = new PdfPCell();
        paymentCell.setBorder(Rectangle.TOP);
        paymentCell.setPaddingTop(10);
        paymentCell.addElement(new Paragraph("Información de pago:", boldFont));

        PdfPTable bankTable = new PdfPTable(2);
        bankTable.setWidthPercentage(100);
        bankTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        bankTable.addCell(new Phrase("Nombre del banco:", normalFont));
        bankTable.addCell(new Phrase("Ensigna", normalFont));
        bankTable.addCell(new Phrase("Número de cuenta:", normalFont));
        bankTable.addCell(new Phrase("0123 4567 8901", normalFont));

        paymentCell.addElement(bankTable);
        footerTable.addCell(paymentCell);

        document.add(footerTable);

        document.close();
        return out.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(new BaseColor(10, 25, 47)); // Dark Blue/Black
        header.setPadding(5);
        header.setPhrase(new Phrase(headerTitle, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header);
    }

    private PdfPCell createCell(String content, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        // cell.setBorder(Rectangle.BOTTOM); // Only bottom borderstyle if preferred
        return cell;
    }

    private void addTotalRow(PdfPTable table, String label, Double value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setPaddingRight(10);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(String.format("$ %.2f", value != null ? value : 0.0)));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }

    private void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }
}
