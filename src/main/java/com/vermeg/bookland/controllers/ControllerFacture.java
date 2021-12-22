package com.vermeg.bookland.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vermeg.bookland.dtos.FactureDto;
import com.vermeg.bookland.repositories.*;
@Controller
@RequestMapping("/facture/")
public class ControllerFacture {
	private final CommandeRepository commandeRepository;
	 public ControllerFacture(CommandeRepository commandeRepository) {
		 
		 this.commandeRepository=commandeRepository;
		 }
	 private String datesearch;
	 //@GetMapping("list")
	 @RequestMapping("/search")
	 public String listfact(Model model,String keyword) {
		 if(keyword!=null) {
			 model.addAttribute("factures", commandeRepository.findFacturepdf(2,keyword));
			 this.datesearch=keyword;
		 }
		 else {
	 model.addAttribute("factures", commandeRepository.findFacture(2));
		 }
	// model.addAttribute("book", new Book());
	 return "facture/Facture";
	 }
	@GetMapping("/createpdf")
	public void exportToPDF(HttpServletResponse response) throws IOException, ParseException {
	    response.setContentType("application/pdf");
	    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	    String currentDateTime = dateFormatter.format(new Date());
	     
	    String headerKey = "Content-Disposition";
	    String headerValue = "attachment; filename=books_" + currentDateTime + ".pdf";
	    response.setHeader(headerKey, headerValue);
	    
	    DateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");
	    String currentDatesearch = dateFormatter2.format(new Date());
	    List<FactureDto> listbook;
	      listbook = commandeRepository.findFacturepdf(2,this.datesearch);
	     System.out.println(this.datesearch );
	    
	    
	    PdfExporter exporter = new PdfExporter(listbook);
	    exporter.export(response);
	     
	}
}
