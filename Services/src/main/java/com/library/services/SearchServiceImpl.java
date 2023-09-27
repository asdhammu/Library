package com.library.services;

import com.library.dto.BookDTO;
import com.library.dto.BookPaginatedDTO;
import com.library.dto.PaginatedDTO;
import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import com.library.exception.NoSuchBorrowerException;
import com.library.nlp.*;
import com.library.repository.AuthorRepository;
import com.library.repository.BookLoanRepository;
import com.library.repository.BookRepository;
import com.library.repository.BorrowerRepository;
import com.library.utils.MapperUtils;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class SearchServiceImpl implements SearchService {

    private static final Logger LOGGER = LogManager.getLogger(BorrowerServiceImpl.class);
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private final BorrowerRepository borrowerRepository;
    private final BookLoanRepository bookLoanRepository;

    NLPQuery nlpQuery;

    public SearchServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, BorrowerRepository borrowerRepository, BookLoanRepository bookLoanRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.borrowerRepository = borrowerRepository;
        this.bookLoanRepository = bookLoanRepository;
        nlpQuery = NLPFactory.getSource(NLPResource.STANFORD);
    }

    @Override
    public BookPaginatedDTO searchBooks(String query, Pageable pageable) {

        if (query.matches("^(\\d{13})?$")) {
            Book book = bookRepository.findById(query).get();
            List<BookDTO> books = new ArrayList<>();
            books.add(MapperUtils.mapBookToBookDTO(book, null));
            return new BookPaginatedDTO(new PaginatedDTO(1, pageable.getPageNumber(), pageable.getPageSize(), 1), books);
        }
        Feature feature = this.nlpQuery.getQuery(query);
        LOGGER.info("Feature is " + feature);
        if (feature.getFeatureType() == FeatureType.AUTHOR) {
            List<com.library.entity.Author> authors = authorRepository.findByNameIgnoreCaseContaining(feature.getQuery());
            Page<Book> bookList = bookRepository.findAllByAuthorsIn(authors, pageable);
            return MapperUtils.mapBooksToBooksDto(bookList);
        } else {
            Page<Book> bookPage = bookRepository.findByTitleIgnoreCaseContaining(feature.getQuery(), pageable);
            return MapperUtils.mapBooksToBooksDto(bookPage);
        }
    }

    @Override
    public BookPaginatedDTO searchBooksForBorrower(String borrowerName, int cardId, String isbn, Pageable pageable) {

        Optional<Borrower> borrower = borrowerRepository.findByCardId(cardId);
        if (borrower.isEmpty()) {
            throw new NoSuchBorrowerException("No borrower exists for this card Id");
        }

        // List<Book> bookList = borrower.get().getBookLoans().stream().filter(y -> y.getDateIn() == null).map(BookLoan::getBook).collect(Collectors.toList());

        Page<BookLoan> bookLoans = bookLoanRepository.findAllByBorrowerAndDateInIsNull(borrower.get(), pageable);
        return MapperUtils.mapBookLoansToBookDTO(bookLoans);
    }
}
