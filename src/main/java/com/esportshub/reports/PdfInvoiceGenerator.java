package com.esportshub.reports;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;

// Generates printable PDF payment receipts using iText 7 library
public class PdfInvoiceGenerator {
    // Creates PDF file in invoices/ folder and returns its file path
    public static String createPdf(String id, String st, String cust, String t, String c, String tot) {
        try {
            new File("invoices").mkdirs();
            String path = "invoices/Invoice_" + id + ".pdf";
            Document doc = new Document(new PdfDocument(new PdfWriter(path)));
            doc.add(new Paragraph("ESPORTS HUB RECEIPT #" + id + "\nStation: " + st + " | Customer: " + cust + "\nTime: " + t + " EGP | Canteen: " + c + " EGP\nTOTAL PAID: " + tot + " EGP"));
            doc.close();
            return path;
        } catch (Exception e) { return null; }
    }
}