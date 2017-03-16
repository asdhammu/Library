package com.library.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.library.model.Author;
import com.library.model.Book;
import com.library.model.BookAuthor;
import com.library.model.Borrower;

public class Callable {

	public static void main(String[] args) {
		
		/**
		 * D://Lectures/Sem 2/DatabaseDesign/Project 1/books.csv
		 * 
		 * D://Lectures/Sem 2/DatabaseDesign/Project 1/borrowers.csv
		 * 
		 */
		Configuration configuration = new Configuration().configure();
        ServiceRegistryBuilder registry = new ServiceRegistryBuilder();
        registry.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = registry.buildServiceRegistry();
         
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        BufferedReader bufferedReader;
        String line = "";
        try {        	
        	int count=0;
        	bufferedReader = new BufferedReader(new FileReader("D://Lectures/Sem 2/DatabaseDesign/Project 1/books.csv"));
        	while((line = bufferedReader.readLine())!=null){
        		
        		String[] s = line.split("\\t");
        		
        		//String[] s = line.split(",");
        		
        		if(s[0].equalsIgnoreCase("isbn10")) continue;
        		
        		//addBorrower(session, s);
        		addBook(session, s);
        		
        	}
			
        	//System.out.println("hello");
		} catch (FileNotFoundException e) {
			System.out.println("error in input" + e);
		} catch (IOException e) {
			e.printStackTrace();
		}
        session.getTransaction().commit();
      
        //session.getTransaction().commit();
        session.close();    	
		
        
        
        
        
        
	}

	private static void addBorrower(Session session, String[] borrowData){
		
		Borrower borrower = new Borrower();
		borrower.setCardId(Integer.parseInt(borrowData[0]));
		borrower.setSsn(borrowData[1]);
		borrower.setbName(borrowData[2]);
		borrower.setAddress(borrowData[5]);
		borrower.setPhone(borrowData[8]);
		
		session.save(borrower);
		
	}
	
	
	private static void addBook(Session session, String[] bookData) {
		
        Book newBook = new Book();
        newBook.setISBN(bookData[1]);
        newBook.setTitle(bookData[2]);
        newBook.setCover(bookData[4]);
        newBook.setPublisher(bookData[5]);
        newBook.setPages(bookData[6]);
       
        session.save(newBook);
               
        String[] names = bookData[3].split(",");
        
        Set<String> set = new HashSet<String>();
        
        for(String s: names){
        	set.add(s);
        }
        
        
        for(String name:set){
        
	        String queryString = "from author where name=:name";
			Query query = session.createQuery(queryString);
			query.setString("name", name); 
			Object object = query.uniqueResult();
	        
			BookAuthor author2 = null;
			
			if(object == null){
				Author author = new Author();
		        author.setName(name);
		        session.save(author);
		        author2 = new BookAuthor(author,newBook);
		       
			}else{
				
				Author author = (Author) object;
				author2 = new BookAuthor(author,newBook);			
			}
			
			session.save(author2);
        }
        
        
        
	}
}
