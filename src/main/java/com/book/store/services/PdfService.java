package com.book.store.services;


import com.book.store.entities.Order;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    private Logger logger = LoggerFactory.getLogger(PdfService.class);

    public ByteArrayInputStream createPdf(Order order){
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25);
            Paragraph title = new Paragraph("Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add customer information
            Font customerFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph customerInfo = new Paragraph("Order Information:\n\n", customerFont);

            customerInfo.add("Name:" + order.getCustomer().getCustomerName() + "\n");
            customerInfo.add("Email: " + order.getCustomer().getCustomerEmailId() + "\n");
            customerInfo.add("Book Title:" + order.getBook().getBookTitle() + "\n");
            customerInfo.add("Book Author: " + order.getBook().getBookAuthor() + "\n");
            customerInfo.add("Book ISBN: " + order.getBook().getBookISBN() + "\n");

            document.add(customerInfo);

            Image bookImage = Image.getInstance(order.getBook().getBookImageUrl());
            bookImage.scaleToFit(200, 200);
            document.add(bookImage);

            HeaderFooter footer = new HeaderFooter(true, new Phrase("Book Store Management System"));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorderWidthBottom(1);
            document.setFooter(footer);


            // Add order details
            Font orderFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph orderDetails = new Paragraph("Order Details:\n\n", orderFont);
            orderDetails.add("Order Number : " + order.getOrderId() + " \n\n");
            orderDetails.add("Order Date : " + order.getOrderDate() + "\n");
            orderDetails.add("Order Status : " + order.getOrderStatus() + "\n");
            orderDetails.add("Delivered Date : " + order.getDeliveredDate() + "\n");
            orderDetails.add("Payment Id : " + order.getTransaction().getPaymentId() + "\n");
            orderDetails.add("Transcation Status : " + order.getTransaction().getTranscationStatus() + "\n");
            document.add(orderDetails);



            // Add itemized list
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            PdfPCell cell1 = new PdfPCell(new Phrase("Item"));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase("Description"));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase("Quantity"));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Phrase("Price"));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell4);

            // Add sample items
            addItemToTable(table, order.getBook().getBookTitle(), order.getTransaction().getReceipt(), order.getBookQuantity(), order.getTransaction().getAmount());

            document.add(table);

            // Add total amount
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph total = new Paragraph("Total: $" + order.getTransaction().getAmount(), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            writer.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static void addItemToTable(PdfPTable table, String item, String description, String quantity, String price) {
        table.addCell(item);
        table.addCell(description);
        table.addCell(String.valueOf(quantity));
        table.addCell(String.valueOf(price));
    }


}
