package com.esportshub.reports;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfInvoiceGenerator {

    public static String generateInvoicePdf(String invoiceId, String stationName, String customerName,
                                            String timeCost, String canteenCost, String totalAmount) {
        try {
            File dir = new File("invoices");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = "invoices/Invoice_" + invoiceId + "_" + System.currentTimeMillis() + ".pdf";
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("ESPORTS HUB & GAMING CENTER")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Official Payment Receipt")
                    .setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("----------------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER));

            String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            document.add(new Paragraph("Invoice #: " + invoiceId));
            document.add(new Paragraph("Date: " + dateStr));
            document.add(new Paragraph("Station / Source: " + stationName));
            document.add(new Paragraph("Customer Name: " + customerName));
            document.add(new Paragraph("----------------------------------------------------------"));
            document.add(new Paragraph("Gaming Time Charge: " + timeCost + " EGP"));
            document.add(new Paragraph("Canteen & Snacks Charge: " + canteenCost + " EGP"));
            document.add(new Paragraph("TOTAL PAID: " + totalAmount + " EGP").setBold().setFontSize(14));
            document.add(new Paragraph("----------------------------------------------------------"));
            document.add(new Paragraph("Thank you for playing at Esports Hub!")
                    .setItalic().setTextAlignment(TextAlignment.CENTER));

            document.close();
            return new File(fileName).getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}