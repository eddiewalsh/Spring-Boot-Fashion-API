package zelora.api.helper;


import com.itextpdf.text.*;
import zelora.api.model.Orders;

import javax.swing.text.Document;

public class PDFHelper {

    public static void addDocumentHeaders(Document document, Orders order) {
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Font boldFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 16, BaseColor.BLACK);
        Paragraph invoice_no_paragraph = new Paragraph();
        Paragraph invoice_order_date = new Paragraph();
        Paragraph invoice_customer_name = new Paragraph();

        Chunk invoicelabel = new Chunk("Invoice: ", boldFont);
        Chunk invoice_no = new Chunk(order.getOrderId().toString(), font);

        invoice_no_paragraph.add(invoicelabel);
        invoice_no_paragraph.add(invoice_no);

        Chunk orderDateLabel = new Chunk("Order Date: ", boldFont);
        Chunk order_date = new Chunk(order.getOrderDate().toString(), font);

        invoice_order_date.add(orderDateLabel);
        invoice_order_date.add(order_date);

        Chunk customerNameLabel = new Chunk("Customer Name: ", boldFont);
        Chunk customerName = new Chunk(order.getCustomerId().getFirstName() + " " + order.getCustomerId().getLastName(), font);

        invoice_customer_name.add(customerNameLabel);
        invoice_customer_name.add(customerName);
    }


}
