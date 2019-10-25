package com.library.initial;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class InitialLoad {


	private static final Logger LOGGER = LogManager.getLogger(InitialLoad.class);

	@Autowired
	BookRepository bookRepository;

	@Autowired
	AuthorRepository authorRepository;

	@PostConstruct
	public void load(){
		
		LOGGER.info("Initial Load started");

        BufferedReader bufferedReader;
        String line;
        try {
        	bufferedReader = new BufferedReader(new FileReader("books.csv"));
        	while((line = bufferedReader.readLine())!=null){
        		String[] s = line.split("\\t");
        		if(s[0].equalsIgnoreCase("isbn10")) continue;
        		addBook(s);
        	}
        	LOGGER.info("Initial Load finished");
		} catch (FileNotFoundException e) {
        	LOGGER.error("Error in input file", e);
		} catch (IOException e) {
			LOGGER.error("Exception occurred", e);
		}
	}
	
	private void addBook(String[] bookData) {
		
        Book book = new Book();
        book.setISBN(bookData[1]);
        book.setTitle(bookData[2]);
        book.setCover(bookData[4]);
        book.setPublisher(bookData[5]);
        book.setPages(bookData[6]);
       
        bookRepository.save(book);
               
        String[] names = bookData[3].split(",");
        
        Set<String> set = new HashSet<>();
        
        for(String s: names){
        	set.add(s);
        }
        
        for(String name:set){
			Author author = authorRepository.findByName(name);
			author.getBooks().add(book);
			authorRepository.save(author);
        }
	}

}
