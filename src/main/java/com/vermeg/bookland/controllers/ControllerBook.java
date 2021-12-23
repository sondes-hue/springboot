package com.vermeg.bookland.controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vermeg.bookland.entities.Book;
import com.vermeg.bookland.entities.Categorie;
import com.vermeg.bookland.repositories.*;


@Controller
@RequestMapping("book")
public class ControllerBook {
    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/uploads";
    public static String pdfDirectory = System.getProperty("user.dir") + "/src/main/resources/static/pdfs";
    private final BookRepository bookRepository;
    private final CategorieRepository categorieRepository;
    private final CommandeRepository commandeRepository;

    @Autowired
    private ServletContext context;

    @Autowired
    public ControllerBook(BookRepository bookRepository, CategorieRepository categorieRepository, CommandeRepository commandeRepository) {
        this.bookRepository = bookRepository;
        this.categorieRepository = categorieRepository;
        this.commandeRepository = commandeRepository;
    }

    @GetMapping("list")
    public String listBook(Model model) {

        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("book", new Book());
        return "book/listbook";
    }

    @GetMapping("add")
    public String showAddBookForm(Book book, Model model) {

        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("book", new Book());
        return "book/addbook";
    }

    @PostMapping("add")
    //@ResponseBody
    public String addBook(RedirectAttributes atts,
                          @Valid Book book,
                          BindingResult result,
                          @RequestParam(name = "categorie_id", required = false) Long p,
                          @RequestParam(value = "files", required = false) MultipartFile[] files,
                          Model model) {
        if (result.hasErrors() || files[0].isEmpty()) {
            model.addAttribute("categories", categorieRepository.findAll());
            if(files[0].isEmpty())
                result.rejectValue("picture", "error.book", "Picture is mandatory");
            return "book/addbook";
        }
        Categorie categorie = categorieRepository.findById(p).orElseThrow(() -> new IllegalArgumentException("Invalid categorie Id:" + p));
        book.setCategorie(categorie);

        /// part upload
        StringBuilder fileName = new StringBuilder();
        MultipartFile file = files[0];
        String datePattern = "yyyy-MM-ddHH-mm-ss";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

        String dateString = simpleDateFormat.format(new Date());

        String filedate = dateString + file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(uploadDirectory, filedate);
        fileName.append(filedate);
        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        book.setPicture(fileName.toString());
        bookRepository.save(book);
        return "redirect:list";
    }

    @GetMapping("delete/{id}")
    public String deletebook(@PathVariable("id") long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book deleteId:" + id));
        try {
            File file = new File(uploadDirectory + "/" + book.getPicture());
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
            System.out.println("Failed to Delete image !!");
        }
        bookRepository.delete(book);
        return "redirect:../list";
    }

    @GetMapping("edit/{id}")
    public String showBookFormToUpdate(@PathVariable("id") long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("book", book);
        model.addAttribute("categories", categorieRepository.findAll());
        return "book/updatebook";
    }

    @PostMapping("update")
    public String updatebook(@RequestParam(name = "pictureBook", required = false) String pic,
                             @RequestParam("files") MultipartFile[] files,
                             @Valid Book book, BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            return "book/updatebook";
        }

        Categorie categ = categorieRepository.findById(book.getCategorie().getId()).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + book.getCategorie().getId()));
        book.setCategorie(categ);
        StringBuilder fileName = new StringBuilder();
        MultipartFile file = files[0];

        if (files[0].isEmpty() == false) {
            String datePattern = "yyyy-MM-ddHH-mm-ss";

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

            String dateString = simpleDateFormat.format(new Date());
            String filedate = dateString + file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDirectory, filedate);
            fileName.append(filedate);
            System.out.println("ddddd" + filedate);
            File filedelete = new File(uploadDirectory + "/" + pic);
            filedelete.delete();
            try {
                Files.write(fileNameAndPath, file.getBytes()); //upload
            } catch (IOException e) {
                e.printStackTrace();
            }
            book.setPicture(fileName.toString());
        }else{
            Path fileNameAndPath = Paths.get(uploadDirectory, pic);
            fileName.append(pic);
            book.setPicture(fileName.toString());
        }
        bookRepository.save(book);

        return "redirect:list";
    }

}
