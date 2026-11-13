package com.esportshub.reports;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;

public class PdfInvoiceGenerator {
    public static String createPdf(String id, String station, String customer, String timeCost, String canteenCost, String total) {
        try {
            new File("invoices").mkdirs();
            String path = "invoices/Invoice_" + id + ".pdf";
            Document doc = new Document(new PdfDocument(new PdfWriter(path)));
            doc.add(new Paragraph("ESPORTS HUB RECEIPT"));
            doc.add(new Paragraph("Invoice ID: " + id));
            doc.add(new Paragraph("Station: " + station + " | Customer: " + customer));
            doc.add(new Paragraph("Time Cost: " + timeCost + " EGP | Canteen: " + canteenCost + " EGP"));
            doc.add(new Paragraph("TOTAL PAID: " + total + " EGP"));
            doc.close();
            return new File(path).getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }
}