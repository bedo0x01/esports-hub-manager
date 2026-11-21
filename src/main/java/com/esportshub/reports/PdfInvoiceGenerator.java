package com.esportshub.reports;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;

public class PdfInvoiceGenerator {
    public static String generate(String id, String station, String customer, String total) {
        try {
            new File("invoices").mkdirs();
            String file = "invoices/Invoice_" + id + ".pdf";
            Document doc = new Document(new PdfDocument(new PdfWriter(file)));
            doc.add(new Paragraph("ESPORTS HUB INVOICE #" + id));
            doc.add(new Paragraph("Station: " + station + " | Player: " + customer));
            doc.add(new Paragraph("Total Paid: " + total + " EGP"));
            doc.close();
            return file;
        } catch (Exception e) {
            return "Error";
        }
    }
}