package com.library.services;

import java.util.List;

import com.library.entity.Book;

public interface BookService {
 
    List<com.library.dto.Book> searchBooks(String query, int page, int size);

	List<com.library.dto.Book> searchBooksForBorrower(String borrowerName, int cardId, String isbn);


}
