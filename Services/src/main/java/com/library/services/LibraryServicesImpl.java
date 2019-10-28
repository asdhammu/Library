package com.library.services;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.modal.BookLoanRequest;
import com.library.modal.CheckInBook;
import com.library.modal.FineResponse;
import com.library.modal.RestResponse;
import com.library.modal.SearchQuery;
import com.library.modal.SearchResult;
import com.library.repository.BookLoanRepository;
import com.library.repository.BookRepository;
import com.library.repository.BorrowerRepository;
import com.library.repository.FineRepository;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LibraryServicesImpl implements LibraryServices {

    private static final Logger LOGGER = LogManager.getLogger(LibraryServicesImpl.class);

    @Autowired
    BorrowerRepository borrowerRepository;

    @Autowired
    BookLoanRepository bookLoanRepository;

    @Autowired
    FineRepository fineRepository;

    @Autowired
    BookRepository bookRepository;


    AbstractSequenceClassifier<CoreLabel> classifier;


    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {

        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    /**
     * Add Borrower
     */
    public RestResponse addBorrower(Borrower borrower) {

        RestResponse response = new RestResponse();

        if (borrowerRepository.findBySsn(borrower.getSsn()).isPresent()) {
            response.setError("Account already exists");
            response.setSuccess(false);
            return response;
        }

        Borrower borrower1 = borrowerRepository.save(borrower);
        response.setSuccess(true);
        response.setResult(String.valueOf(borrower1.getCardId()));
        return response;

    }

    public List<FineResponse> getAllFines() {

        Session session = this.sessionFactory.openSession();
        StringBuilder builder = new StringBuilder();

        builder.append("select b.card_id , sum(fine_amt) from fine f ,book_loan b where b.loan_id= f.loan_id and f.paid=0 group by b.card_id");

        Query query = session.createSQLQuery(builder.toString());

        List<Object[]> object = query.list();

        List<FineResponse> fineResponses = new ArrayList<>();

        for (Object[] object2 : object) {
            FineResponse fineResponse = new FineResponse();
            fineResponse.setCardId((Integer) object2[0]);
            fineResponse.setAmount(Double.toString((Double) object2[1]));

            fineResponses.add(fineResponse);
        }

        session.close();


        return fineResponses;
    }

    public RestResponse addFine() {

        RestResponse response = new RestResponse();

        try {
            List<BookLoan> bookLoans = bookLoanRepository.findAllByDateInIsNull();
            Fine fine = null;
            LocalDateTime date = LocalDateTime.now();
            for (BookLoan bookLoan : bookLoans) {

                if (bookLoan.getDueDate().isBefore(date)) {

                    if (bookLoan.getFine() == null) {
                        fine = new Fine(bookLoan);
                    } else {
                        fine = bookLoan.getFine();
                    }
                    long daysOverDue = Duration.between(date.toLocalDate(), bookLoan.getDueDate().toLocalDate()).toDays();
                    fine.setFineAmount(Double.toString(daysOverDue * 0.25));
                    fineRepository.save(fine);
                }
            }
            response.setSuccess(true);
            response.setResult("Fine updated successfully");

        } catch (Exception e) {
            response.setError("Error while updating. Try Again");
            response.setSuccess(false);
        }

        return response;
    }

    @Override
    public RestResponse payFine(int cardId) {
        RestResponse response = new RestResponse();
        Borrower borrower = borrowerRepository.findByCardId(cardId).get();
        if (borrower == null) {
            response.setError("No such borrower");
            response.setSuccess(false);
            return response;
        }

        List<BookLoan> bookLoans = borrower.getBookLoans();
        if (bookLoans.isEmpty()) {
            response.setError("This borrower has not loaned this book");
            response.setSuccess(false);
            return response;
        }

        for (BookLoan bookLoan : bookLoans) {
            bookLoan.getFine().setPaid(true);
            bookLoanRepository.save(bookLoan);
        }

        response.setResult("Fine paid successfully");
        response.setSuccess(true);
        return response;
    }

    @Override
    public RestResponse checkInBook(CheckInBook book) {

        RestResponse response = new RestResponse();

        try {
            /*BookBorrowerPrimaryKey bookBorrowerPrimaryKey = new BookBorrowerPrimaryKey();
            bookBorrowerPrimaryKey.setIsbn(book.getIsbn());
            bookBorrowerPrimaryKey.setCardId(book.getCardId());*/
            bookRepository.findByIsbn(book.getIsbn()).get();
            BookLoan bookLoan = bookLoanRepository.findByBorrowerAndBook(borrowerRepository.findByCardId(book.getCardId()).get(),
                    bookRepository.findByIsbn(book.getIsbn()).get()).get(0);
            bookLoan.setDateIn(LocalDateTime.now());
            Book book1 = bookRepository.findByIsbn(book.getIsbn()).get();
            book1.setAvailable(true);
            bookRepository.save(book1);
            bookLoanRepository.save(bookLoan);

            response.setSuccess(true);
            response.setResult("Book Checked In Succesfully");
        } catch (Exception exception) {

            response.setSuccess(false);
            response.setError("Error Occurred. Try after some time");
        }

        return response;

    }

    public List<SearchResult> searchCheckedInBooks(CheckInBook book) {

        if (book.getIsbn().length() > 0 && book.getIsbn().length() == 13) {
            SearchQuery query = new SearchQuery();
            query.setQuery(book.getIsbn());
            List<SearchResult> result = new ArrayList<>();

            for (SearchResult result2 : this.search(query)) {
                if (!result2.isAvailable()) {
                    result.add(result2);
                }
            }
            return result;
        }

        String name = book.getName();
        int cardId = book.getCardId();

        List<Borrower> borrower = new ArrayList<>();

        if (name.length() > 0 && cardId != 0) {
            /*queryString = "from Borrower where cardId=" + cardId + " or bName like '%" + name
                    + "%'";
*/
            borrower.addAll(borrowerRepository.findByCardIdOrBNameIgnoreCaseContaining(cardId, name));

        } else if (name.length() > 0) {
            //queryString = "from Borrower where bName like '%" + name + "%'";
            borrower.addAll(borrowerRepository.findByBNameIgnoreCaseContaining(name));
        } else if (name.length() == 0) {
            //queryString = "from Borrower where cardId=" + cardId;

            borrower.add(borrowerRepository.findByCardId(cardId).get());
        }
        List<SearchResult> list = new ArrayList<>();
        for (Borrower borrower2 : borrower) {

            List<BookLoan> bookLoans = borrower2.getBookLoans();

            for (BookLoan bookLoan : bookLoans) {
                SearchResult result = new SearchResult();
                if (bookLoan.getDateIn() == null) {
                    Book book1 = bookRepository.findByIsbn(bookLoan.getBook().getIsbn()).get();

                    result.setCover(book1.getCover());
                    result.setISBN(book1.getIsbn());
                    result.setTitle(book1.getTitle());
                    result.setBorrower(borrower2);
                    list.add(result);
                }

            }
        }

        return list;


    }

    @Override
    public RestResponse addBookLoan(BookLoanRequest bookLoanRequest) {

        String isbn = bookLoanRequest.getIsbn();
        int borrowerId = bookLoanRequest.getBorrowerId();
        RestResponse response = new RestResponse();

        if (!borrowerRepository.findByCardId(borrowerId).isPresent()) {
            response.setError("Borrower not in the database");
            response.setSuccess(false);
            return response;
        }

        List<BookLoan> bookLoans = bookLoanRepository.findByBorrower(borrowerRepository.findByCardId(borrowerId).get()).stream().filter(x -> x.getDateIn() == null)
                .collect(Collectors.toList());
        if (bookLoans.size() > 2) {
            response.setError("3 Book have been issued to the borrower");
            response.setSuccess(false);
            return response;
        }

        Book book = bookRepository.findByIsbn(isbn).get();
        if (book == null || !book.isAvailable()) {
            response.setSuccess(false);
            response.setError("Book is not available. It's checked out");
            return response;
        }

        Borrower borrower = borrowerRepository.findByCardId(borrowerId).get();
        Book  book1 = bookRepository.findByIsbn(isbn).get();
        BookLoan bookLoan = new BookLoan(borrower, book1);
        bookLoan.setDateOut(LocalDateTime.now());
        bookLoan.setDueDate(LocalDateTime.now().minusDays(-14));
        book.setAvailable(false);
        bookRepository.save(book);
        response.setResult("Added Successfully");
        response.setSuccess(true);

        return response;
    }

    /**
     * Searches book catalog and returns result with author details and
     * availability
     */
    public List<SearchResult> search(SearchQuery searchQuery) {

        List<SearchResult> result = new ArrayList<>();

        // regex for 13 digits i.e. ISBN number with 13 digits
        if (searchQuery.getQuery().matches("^(\\d{13})?$")) {

            Book book = bookRepository.findByIsbn(searchQuery.getQuery()).get();
            List<Book> books = new ArrayList<>();
            books.add(book);
            prepareSearchResult(result, books);
            return result;
        }

        StringBuffer bookNameQuery = new StringBuffer();
        StringBuffer authorQuery = new StringBuffer();

        StringBuilder capitalizeString = new StringBuilder();
        String[] str = searchQuery.getQuery().split(" ");
        for (int i = 0; i < str.length; i++) {
            capitalizeString.append(str[i].substring(0, 1).toUpperCase() + str[i].substring(1) + " ");
        }
        capitalizeString.deleteCharAt(capitalizeString.length() - 1);
        searchQuery.setQuery(capitalizeString.toString());

        extractNameUsingStanfordNLP(searchQuery, bookNameQuery, authorQuery);

/*
        hqlQuery = "from Book b, author a, BookAuthor ba where a.author_id= ba.author.author_id and b.ISBN = ba.book.ISBN and b.title like '%"
                + bookNameQuery + "%' and a.name like '%" + authorQuery + "%'";
*/

        List<Book> books = bookRepository.findByTitleIgnoreCaseContaining(bookNameQuery.toString());

        prepareSearchResult(result, books);

        return result;
    }

    /**
     * Prepares the result
     *
     * @param result
     * @param list
     */
    private void prepareSearchResult(List<SearchResult> result, List<Book> list) {
        Book book;
        Author author;
        List<Author> authorList;

        Map<String, SearchResult> isbn = new HashMap<>();

        for (Object b : list) {

            Object[] arr = (Object[]) b;

            book = (Book) arr[0];
            author = (Author) arr[1];

            if (isbn.containsKey(book.getIsbn())) {
                authorList = isbn.get(book.getIsbn()).getAuthor();
                authorList.add(author);
                SearchResult searchResult = isbn.get(book.getIsbn());
                searchResult.setAuthor(authorList);
                isbn.put(book.getIsbn(), searchResult);

            } else {
                SearchResult result2 = new SearchResult();
                result2.setCover(book.getCover());
                result2.setAvailable(book.isAvailable());
                result2.setISBN(book.getIsbn());
                result2.setTitle(book.getTitle());
                result2.setPublisher(book.getPublisher());
                result2.setPages(book.getPages());
                List<Author> authors = new ArrayList<Author>();
                authors.add(author);
                result2.setAuthor(authors);
                isbn.put(book.getIsbn(), result2);
            }
        }

        for (Map.Entry<String, SearchResult> map : isbn.entrySet()) {
            result.add(map.getValue());
        }
    }

    /**
     * Using Natural Language processing for extracting name entities
     *
     * @param searchQuery
     * @param bookNameQuery
     * @param authorQuery
     */
    private void extractNameUsingStanfordNLP(SearchQuery searchQuery, StringBuffer bookNameQuery,
                                             StringBuffer authorQuery) {
        try {

            int startIndex = 0, endIndex = 0;
            List<Triple<String, Integer, Integer>> triples = classifier
                    .classifyToCharacterOffsets(searchQuery.getQuery());
            for (Triple<String, Integer, Integer> trip : triples) {
                startIndex = trip.second();
                endIndex = trip.third();

            }

            authorQuery.append(searchQuery.getQuery(), startIndex, endIndex);
            bookNameQuery.append(searchQuery.getQuery(), 0, startIndex);
            bookNameQuery.append(searchQuery.getQuery().substring(endIndex));

            LOGGER.debug("Author Query " + authorQuery + " :: Book query ::  " + bookNameQuery);

        } catch (Exception e) {
            LOGGER.error("Exception while extracting features ", e);
        }

    }

    @SuppressWarnings("unused")
    private void extractNameUsingApacheNLP(SearchQuery searchQuery, StringBuffer bookNameQuery,
                                           StringBuffer authorQuery) {
        InputStream modelFile = null;

        String fileName = "en-ner-person.bin";

        try {
            modelFile = new FileInputStream(new File(fileName));
            TokenNameFinderModel finderModel = new TokenNameFinderModel(modelFile);
            NameFinderME finderME = new NameFinderME(finderModel);
            String[] sQuery = searchQuery.getQuery().split(" ");
            Span nameSpan[] = finderME.find(sQuery);
            int start = 0, end = 0, diff = 0;
            for (Span s : nameSpan) {

                System.out.println(s.toString());
                start = s.getStart();
                end = s.getEnd();
                diff = s.getEnd() - s.getStart();
                for (int i = 0; i < diff; i++) {
                    authorQuery.append(sQuery[s.getStart() + i]);
                    if (i == diff - 1) {
                        continue;
                    }
                    authorQuery.append(" ");
                }
            }

            for (int i = 0; i < sQuery.length; i++) {

                if (i < start || i >= end) {
                    bookNameQuery.append(sQuery[i]);
                    if (i == sQuery.length - 1) {
                        continue;
                    }
                    bookNameQuery.append(" ");
                }
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("File not found", e);
        } catch (IOException e) {
            LOGGER.error("Exception occurred", e);
        }

    }

    @Override
    public List<Fine> getFineForCardId(SearchQuery searchQuery) {
        boolean isPaid = Boolean.parseBoolean(searchQuery.getPaid());
        Borrower borrower = borrowerRepository.findByCardId(Integer.parseInt(searchQuery.getQuery())).get();
        List<BookLoan> bookLoans = borrower.getBookLoans();

        List<Fine> fines = new ArrayList<>();
        for (BookLoan bookLoan : bookLoans) {
            if (bookLoan.getFine() != null) {
                if (isPaid && bookLoan.getFine().isPaid()) {
                    fines.add(bookLoan.getFine());
                }
                if (!isPaid && !bookLoan.getFine().isPaid()) {
                    fines.add(bookLoan.getFine());
                }
            }
        }

        return fines;
    }


    @PostConstruct
    public void loadClassifier(){
        try {
            LOGGER.debug("Classifier loading started");
            ClassLoader classLoader = LibraryServicesImpl.class.getClassLoader();
            File file = new File(classLoader.getResource("nlp/english.all.3class.distsim.crf.ser.gz").getFile());
            this.classifier = CRFClassifier.getClassifier(file);
            LOGGER.debug("Classifier loaded successfully");
        } catch (Exception e){
            LOGGER.error("Error while loading classifier", e);
        }
    }

}
