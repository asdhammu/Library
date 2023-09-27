package com.library.initial;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.Configuration;
import com.library.modal.Status;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.repository.ConfigurationRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class InitialLoad {

    private static final Logger LOGGER = LogManager.getLogger(InitialLoad.class);

    final
    BookRepository bookRepository;

    final
    ConfigurationRepository configurationRepository;

    final
    AuthorRepository authorRepository;

    public InitialLoad(BookRepository bookRepository, ConfigurationRepository configurationRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.configurationRepository = configurationRepository;
        this.authorRepository = authorRepository;
    }

    public void load() {

        if (!configurationRepository.findAll().isEmpty() && configurationRepository.findAll().get(0).getStatus().equals(Status.COMPLETED)) {
            LOGGER.info("Data has been loaded");
            return;
        }

        LOGGER.info("Initial Load started");

        BufferedReader bufferedReader;
        String line;
        try {
            ClassLoader classLoader = InitialLoad.class.getClassLoader();
            bufferedReader = new BufferedReader(new FileReader(classLoader.getResource("books-lite.csv").getFile()));
            Configuration configuration = new Configuration();
            configuration.setStatus(Status.COMPLETED);
            configurationRepository.save(configuration);
            while ((line = bufferedReader.readLine()) != null) {
                String[] s = line.split("\\t");
                if (s[0].equalsIgnoreCase("isbn10")) continue;
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

        if (bookRepository.findById(bookData[1]).isPresent()) {
            return;
        }


        Book book = new Book();
        book.setIsbn(bookData[1]);
        book.setTitle(bookData[2]);
        book.setCover(bookData[4]);
        book.setPublisher(bookData[5]);
        book.setPages(bookData[6]);
        book.setAvailable(true);
        book = bookRepository.saveAndFlush(book);
        String[] names = bookData[3].split(",");

        Set<String> set = new HashSet<>();
        Collections.addAll(set, names);
        try {

            List<Author> authorList = new ArrayList<>();
            for (String name : set) {
                List<Author> authors = authorRepository.findByName(name);
                Author author;
                if (authors.isEmpty()) {
                    author = new Author();
                    author.setName(name);
                } else {
                    author = authors.get(0);
                }
                author.addBook(book);
                authorList.add(author);
            }
            authorRepository.saveAll(authorList);
        } catch (Exception e) {
            LOGGER.error("Book name" + book.getIsbn() + " - " + book.getTitle());
            throw e;
        }
    }

}
