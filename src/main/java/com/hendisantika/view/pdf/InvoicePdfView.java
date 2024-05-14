package com.hendisantika.view.pdf;

import com.hendisantika.model.Invoice;
import com.hendisantika.model.InvoiceLine;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.awt.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-invoice-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 15/09/21
 * Time: 05.23
 */
@Component("invoices/view")
public abstract class InvoicePdfView extends AbstractPdfView {
    protected void buildPdfDocument(Map<String, Object> model, Document document,
                                    PdfWriter writer, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        MessageSourceAccessor messages = getMessageSourceAccessor();

        Invoice invoice = (Invoice) model.get("invoice");

        PdfPCell cell = new PdfPCell();

        PdfPTable clientDataTable = new PdfPTable(1);
        clientDataTable.setSpacingAfter(20);
        cell = new PdfPCell(new Phrase(messages.getMessage("text.invoice.view.data.client")));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        clientDataTable.addCell(cell);
        clientDataTable.addCell(invoice.getClient().getName() + " " + invoice.getClient().getSurname());
        clientDataTable.addCell(invoice.getClient().getEmail());

        PdfPTable invoiceDataTable = new PdfPTable(1);
        invoiceDataTable.setSpacingAfter(20);
        cell = new PdfPCell(new Phrase(messages.getMessage("text.invoice.view.data.invoice")));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);
        invoiceDataTable.addCell(cell);
        invoiceDataTable.addCell(messages.getMessage("text.invoice.id") + ": " + invoice.getId());
        invoiceDataTable.addCell(messages.getMessage("text.invoice.description") + ": " + invoice.getDescription());
        invoiceDataTable.addCell(messages.getMessage("text.invoice.created_at") + ": " + invoice.getCreatedAt());

        document.add(clientDataTable);
        document.add(invoiceDataTable);

        PdfPTable productsTable = new PdfPTable(4);
        productsTable.setWidths(new float[]{3.5f, 1, 1, 1});
        productsTable.setSpacingAfter(20);
        productsTable.addCell(messages.getMessage("text.product.product"));
        productsTable.addCell(messages.getMessage("text.product.price"));
        productsTable.addCell(messages.getMessage("text.product.quantity"));
        productsTable.addCell(messages.getMessage("text.product.total"));
        for (InvoiceLine line : invoice.getLines()) {
            productsTable.addCell(line.getProduct().getName());
            productsTable.addCell(line.getProduct().getPrice().toString());
            cell = new PdfPCell(new Phrase(line.getQuantity().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            productsTable.addCell(cell);
            cell = new PdfPCell(new Phrase(line.calculatePrice().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            productsTable.addCell(cell);
        }
        cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        productsTable.addCell(cell);

        productsTable.addCell(invoice.getTotal().toString());

        document.add(productsTable);
    }
}
