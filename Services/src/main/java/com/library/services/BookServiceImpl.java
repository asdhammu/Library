package com.library.services;

import com.library.dto.Author;
import com.library.entity.Book;
import com.library.entity.Borrower;
import com.library.error.*;

import com.library.nlp.*;
import com.library.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import java.util.*;
import java.util.stream.Collectors;
public class BookServiceImpl implements BookService {
    private static final Logger LOGGER = LogManager.getLogger(BookServiceImpl.class);

    @Autowired
    BorrowerRepository borrowerRepository;

    @Autowired
    BookLoanRepository bookLoanRepository;

    @Autowired
    FineRepository fineRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    NLPQuery nlpQuery;

    @Override
    public List<com.library.dto.Book> searchBooks(String query, int page, int size) {

        if (query.matches("^(\\d{13})?$")) {
            Book book = bookRepository.findById(query).get();
            List<Book> books = new ArrayList<>();
            books.add(book);
            return mapBooksToBooksDto(books);
        }
        Feature feature = this.nlpQuery.getQuery(query);
        LOGGER.info("Feature is " + feature);
        if (feature.getFeatureType() == FeatureType.AUTHOR) {
            List<com.library.entity.Author> authors = authorRepository.findByNameIgnoreCaseContaining(feature.getQuery());
            List<Book> bookList = authors.stream().flatMap(x -> x.getBooks().stream()).collect(Collectors.toList());
            return mapBooksToBooksDto(bookList);
        } else {
            if (size > 100) {
                size = 100;
            }
            return mapBooksToBooksDto(bookRepository.findByTitleIgnoreCaseContaining(feature.getQuery(), PageRequest.of(page, size)).getContent());
        }
    }
     @Override
    public List<com.library.dto.Book> searchBooksForBorrower(String borrowerName, int cardId, String isbn) {

        Optional<Borrower> borrower = borrowerRepository.findByCardId(cardId);
        if (!borrower.isPresent()) {
            throw new NoSuchBorrowerException("No borrower exists for this card Id");
        }

        List<Book> bookList = borrower.get().getBookLoans().stream().filter(y -> y.getDateIn() == null).map(x -> x.getBook()).collect(Collectors.toList());

        List<com.library.dto.Book> list = mapBooksToBooksDto(bookList);
        list.stream().forEach(x -> {
            com.library.dto.Borrower borrower1 = new com.library.dto.Borrower();
            borrower1.setName(borrower.get().getbName());
            borrower1.setCardId(borrower.get().getCardId());
            x.setBorrower(borrower1);
        });
        return list;
    }

    /**
     * Map books to books DTO
     *
     * @param books
     * @return
     */
    private List<com.library.dto.Book> mapBooksToBooksDto(List<Book> books) {

        List<com.library.dto.Book> list = new ArrayList<>();

        books.forEach(x -> {

            com.library.dto.Book book = new com.library.dto.Book();
            book.setISBN(x.getIsbn());
            book.setTitle(x.getTitle());
            book.setCover(x.getCover());
            book.setPages(x.getPages());
            book.setAvailable(x.isAvailable());
            List<Author> author = new ArrayList<>();
            x.getAuthors().forEach(y -> {
                Author author1 = new Author();
                author1.setName(y.getName());
                author.add(author1);
            });

            book.setAuthor(author);
            list.add(book);
        });


        return list;

    }
}
