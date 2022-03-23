package com.library.initial;
import com.library.entity.Author;
import com.library.entity.Book;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoadBook extends InitialLoad{

    String[] bookData;
    
public LoadBook(String[] bookData)
{
    addBook(this.bookData);
}
public void addBook(String[] bookData) {

    if (bookRepository.findById(bookData[1]).isPresent()) {
        return;
    }


    Book book = new Book();
    book.setIsbn(bookData[1]);
    book.setTitle(bookData[2]);
    book.setCover(bookData[4]);
    book.setPublisher(bookData[5]);
    book.setPages(bookData[6]);
    book = bookRepository.saveAndFlush(book);
    String[] names = bookData[3].split(",");

    Set<String> set = new HashSet<>();

    for (String s : names) {
        set.add(s);
    }

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
        System.out.println("Book name" + book.getIsbn() + " - " + book.getTitle());
        System.out.println(book.getAuthors());
        throw e;
    }
}
}
