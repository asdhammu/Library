package com.library.utils;

import com.library.dto.*;
import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class MapperUtils {
    /**
     * Map books to books DTO
     */
    public static BookPaginatedDTO mapBooksToBooksDto(Page<Book> books) {
        List<BookDTO> bookDTOS = new ArrayList<>();
        books.getContent().forEach(bo -> {
            BookDTO bookDTO = mapBookToBookDTO(bo, null);
            bookDTOS.add(bookDTO);
        });

        PaginatedDTO paginatedDTO = new PaginatedDTO(books.getTotalPages(), books.getNumber(), books.getSize(), books.getTotalElements());
        return new BookPaginatedDTO(paginatedDTO, bookDTOS);
    }

    public static BookPaginatedDTO mapBookLoansToBookDTO(Page<BookLoan> bookLoans) {

        List<BookDTO> books = new ArrayList<>();
        bookLoans.getContent().forEach(bookLoan -> {
            books.add(mapBookToBookDTO(bookLoan.getBook(), bookLoan.getBorrower()));
        });
        PaginatedDTO paginatedDTO = new PaginatedDTO(bookLoans.getTotalPages(), bookLoans.getNumber(), bookLoans.getSize(), bookLoans.getTotalElements());
        return new BookPaginatedDTO(paginatedDTO, books);

    }

    public static BorrowerDTO mapBorrowerToBorrowerDTO(Borrower borrower) {
        BorrowerDTO borrowerDTO = new BorrowerDTO();
        borrowerDTO.setName(borrower.getbName());
        borrowerDTO.setCardId(borrower.getCardId());
        return borrowerDTO;
    }

    public static BookDTO mapBookToBookDTO(Book book, Borrower borrower) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setISBN(book.getIsbn());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setCover(book.getCover());
        bookDTO.setPages(book.getPages());
        bookDTO.setAvailable(book.isAvailable());
        List<AuthorDTO> authorDTOS = new ArrayList<>();
        book.getAuthors().forEach(y -> {
            AuthorDTO authorDTO1 = new AuthorDTO();
            authorDTO1.setName(y.getName());
            authorDTOS.add(authorDTO1);
        });
        bookDTO.setAuthors(authorDTOS);

        if (borrower != null) {
            bookDTO.setBorrower(mapBorrowerToBorrowerDTO(borrower));
        }
        return bookDTO;
    }
}
