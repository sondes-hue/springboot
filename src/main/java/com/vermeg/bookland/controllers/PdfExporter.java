package com.vermeg.bookland.controllers;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.vermeg.bookland.dtos.FactureDto;


public class PdfExporter {
private List<FactureDto> listbooks;

public PdfExporter(List<FactureDto> listbooks) {
	super();
	this.listbooks = listbooks;
}
private void writeTableHeader(PdfPTable table) {
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(Color.BLUE);
    cell.setPadding(5);
     
    Font font = FontFactory.getFont(FontFactory.HELVETICA);
    font.setColor(Color.WHITE);
     
    cell.setPhrase(new Phrase("title", font));
     
    table.addCell(cell);
     
    cell.setPhrase(new Phrase("author", font));
    table.addCell(cell);
     
    cell.setPhrase(new Phrase("price", font));
    table.addCell(cell);
    
}
private void writeTableData(PdfPTable table) {
    for (FactureDto fac : listbooks) {
        
        table.addCell(fac.getTitle());
        table.addCell(String.valueOf(fac.getCommande()));
        table.addCell(String.valueOf(fac.getPrice()));
    }
}


public void export(HttpServletResponse response) throws IOException {
    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, response.getOutputStream());
     
    document.open();
    Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    font.setSize(18);
    font.setColor(Color.BLUE);
     
    Paragraph p = new Paragraph("Facture", font);
    p.setAlignment(Paragraph.ALIGN_CENTER);
     
    document.add(p);
     
    PdfPTable table = new PdfPTable(3);
    table.setWidthPercentage(100f);
    table.setWidths(new float[] {3.5f, 3.5f, 3.0f});
    table.setSpacingBefore(10);
     
    writeTableHeader(table);
    writeTableData(table);
     
    document.add(table);
     
    document.close();
     
}


}
