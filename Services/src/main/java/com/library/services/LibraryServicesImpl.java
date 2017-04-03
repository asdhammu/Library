package com.library.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.library.model.Author;
import com.library.model.Book;
import com.library.model.BookLoan;
import com.library.model.Borrower;
import com.library.model.Fine;
import com.library.rest.BookLoanRequest;
import com.library.rest.CheckInBook;
import com.library.rest.FineResponse;
import com.library.rest.RestResponse;
import com.library.rest.SearchQuery;
import com.library.rest.SearchResult;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

public class LibraryServicesImpl implements LibraryServices {

	private SessionFactory sessionFactory;

	AbstractSequenceClassifier<CoreLabel> classifier;

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
		Session session = this.sessionFactory.openSession();
		Transaction transaction = (Transaction) session.beginTransaction();

		String queryString = "from Borrower where ssn=:ssn";
		Query query = session.createQuery(queryString);
		query.setString("ssn", borrower.getSsn());
		Object object = query.uniqueResult();

		if (object != null) {
			response.setError("Account already exists");
			response.setSuccess(false);

		} else {

			session.persist(borrower);
			transaction.commit();

			query = session.createQuery(queryString);
			query.setString("ssn", borrower.getSsn());
			object = query.uniqueResult();

			Borrower borrower2 = (Borrower) object;
			response.setSuccess(true);
			response.setResult(String.valueOf(borrower2.getCardId()));
		}

		session.close();

		return response;

	}

	public List<FineResponse> getAllFines(){
		
		//System.out.println("Paid " + paid);
		Session session = this.sessionFactory.openSession();
		//Transaction transaction = session.beginTransaction();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("select b.card_id , sum(fine_amt) from fine f ,book_loan b where b.loan_id= f.loan_id and f.paid=0");
		
		/*if(paid){
			builder.append(" and f.paid=1");
		
		}*/
		
		builder.append(" group by b.card_id");
		
		Query query = session.createSQLQuery(builder.toString());
		
		List<Object[]> object = query.list();
		
		List<FineResponse> fineResponses = new ArrayList<FineResponse>();
		
		for(Object[] object2:object){
			FineResponse fineResponse = new FineResponse();
			fineResponse.setCardId((Integer)object2[0]);
			fineResponse.setAmount(Double.toString((Double)object2[1]));
			
			fineResponses.add(fineResponse);
		}
		
		session.close();
		
		
		return fineResponses;
	}
	
	public RestResponse addFine() {

		RestResponse response = new RestResponse();
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		try {
			String paramString = "from BookLoan where dateIn=null";
			//String paramString = "select * from book_loan where date_in is null";
			Query query = session.createQuery(paramString);

			List<BookLoan> bookLoans = query.list();

			Fine fine = null;
			Date date = new Date();
			for (BookLoan bookLoan : bookLoans) {

				if (bookLoan.getDueDate().before(date)) {

					
					if(bookLoan.getFine()==null){
						fine = new Fine(bookLoan);
					}else{
						fine = bookLoan.getFine();
					}
					
					long diff = date.getTime() - bookLoan.getDueDate().getTime();

					long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					fine.setFineAmount(Double.toString(days * 0.25));

					session.saveOrUpdate(fine);
				}

			}
			transaction.commit();
			response.setSuccess(true);
			response.setResult("Fine updated successfully");

		} catch (Exception e) {
			transaction.rollback();
			response.setError("Error while updating. Try Again");
			response.setSuccess(false);
		}

		session.close();

		return response;
	}

	public RestResponse payFine(int cardId) {

		RestResponse response = new RestResponse();
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		String paramString = "from Fine where fineId="+ cardId;// + " and dateIn !=null";
		Query query = session.createQuery(paramString);
		
		Fine fine = (Fine) query.uniqueResult();
		
		if(fine!=null && fine.getBookLoan().getDateIn()!=null){
			fine.setPaid(true);
			session.save(fine);
			transaction.commit();
			response.setResult("Fine paid successfully");
			
		}else{
			response.setResult("Book is not checked in");
		}
		
		
		session.close();
		 
		return response;
	}

	public RestResponse checkInBook(CheckInBook book) {

		RestResponse response = new RestResponse();
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {

			String paramString = "from BookLoan where book.ISBN='" + book.getIsbn() + "' and borrower.cardId="
					+ Integer.parseInt(book.getCardId());
			Query query = session.createQuery(paramString);

			BookLoan bookLoan = (BookLoan) query.uniqueResult();

			bookLoan.setDateIn(new Date());

			bookLoan.getBook().setAvailable(true);

			session.update(bookLoan);
			transaction.commit();

			response.setSuccess(true);
			response.setResult("Book Checked In Succesfully");
		} catch (Exception exception) {

			response.setSuccess(false);
			response.setError("Error Occurred. Try after some time");
			transaction.rollback();
		}

		session.close();

		return response;

	}

	public List<SearchResult> searchCheckedInBooks(CheckInBook book) {

		// System.out.println("Card id : " + book.getCardId() + " ID " +
		// book.getCardID() + " name" + book.getName());
		if (book.getIsbn().length() > 0 && book.getIsbn().length() == 13) {
			SearchQuery query = new SearchQuery();
			query.setQuery(book.getIsbn());
			List<SearchResult> result = new ArrayList<SearchResult>();

			for (SearchResult result2 : this.search(query)) {
				if (!result2.isAvailable()) {
					result.add(result2);
				}
			}

			return result;
		} else {

			String name = book.getName();
			String cardId = book.getCardId();

			Session session = this.sessionFactory.openSession();
			// Transaction transaction = session.beginTransaction();
			String queryString = "";
			if (name.length() > 0 && cardId.length() > 0) {
				queryString = "from Borrower where cardId=" + Integer.parseInt(cardId) + " or bName like '%" + name
						+ "%'";
			} else if (cardId.length() == 0 && name.length() > 0) {
				queryString = "from Borrower where bName like '%" + name + "%'";
			} else if (cardId.length() > 0 && name.length() == 0) {
				queryString = "from Borrower where cardId=" + Integer.parseInt(cardId);
			}

			Query query = session.createQuery(queryString);

			Object object = query.list();

			List<SearchResult> list = new ArrayList<SearchResult>();

			if (object == null) {
				return list;
			}
			List<Borrower> borrower = (List<Borrower>) object;

			for (Borrower borrower2 : borrower) {

				List<BookLoan> bookLoans = borrower2.getBookLoans();

				for (BookLoan bookLoan : bookLoans) {
					SearchResult result = new SearchResult();
					if (bookLoan.getDateIn() == null) {
						result.setCover(bookLoan.getBook().getCover());
						result.setISBN(bookLoan.getBook().getISBN());
						result.setTitle(bookLoan.getBook().getTitle());
						result.setBorrower(borrower2);
						list.add(result);
					}

				}

			}

			session.close();
			return list;
		}

	}

	public RestResponse addBookLoan(BookLoanRequest bookLoanRequest) {

		System.out.println(bookLoanRequest.getBorrowerId());
		System.out.println(bookLoanRequest.getIsbn());

		String isbn = bookLoanRequest.getIsbn();
		String borrowerId = bookLoanRequest.getBorrowerId();
		RestResponse response = new RestResponse();
		String borrowerQuery = "from Borrower where cardId=" + Integer.parseInt(borrowerId);

		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery(borrowerQuery);
		Object object = query.uniqueResult();

		if (object == null) {

			response.setError("Borrower not in the database");
			response.setSuccess(false);

		} else {

			Borrower borrower = (Borrower) object;

			String loanQuery = "from BookLoan where borrower=" + Integer.parseInt(borrowerId) + " and dateIn IS null";
			Query query2 = session.createQuery(loanQuery);
			List<BookLoan> bookLoans = query2.list();
			
			if (bookLoans.size() > 2) {
				response.setError("3 Book have been issued to the borrower");
				response.setSuccess(false);

			} else {	

				try {
					String bookQuery = "from Book where ISBN=" + isbn;
					Query bookQ = session.createQuery(bookQuery);

					Object object2 = bookQ.uniqueResult();

					Book book = (Book) object2;

					if (book == null || !book.isAvailable()) {
						response.setSuccess(false);
						response.setError("Book is not available. It's checked out");

					} else {

						BookLoan bookLoan = new BookLoan(book, borrower);

						Date date = new Date();
						bookLoan.setDateOut(date);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						calendar.add(Calendar.DAY_OF_YEAR, 14);

						bookLoan.setDueDate(calendar.getTime());

						session.persist(bookLoan);
						response.setResult("Added Succesfull");
						response.setSuccess(true);

						book.setAvailable(false);
						session.update(book);

						transaction.commit();
					}

				} catch (Exception e) {
					response.setSuccess(false);
					response.setError("Issue in Database");
					transaction.rollback();

				}
			}
		}

		session.close();

		return response;
	}

	/**
	 * Searches book catalog and returns result with author details and
	 * availability
	 */
	public List<SearchResult> search(SearchQuery searchQuery) {

		List<SearchResult> result = new ArrayList<SearchResult>();
		String hqlQuery = "";
		Session session = this.sessionFactory.openSession();

		if (searchQuery.getQuery().matches("^([0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])")) {

			hqlQuery = "from Book b, author a, BookAuthor ba where a.author_id= ba.author.author_id and b.ISBN = ba.book.ISBN and b.ISBN="
					+ searchQuery.getQuery();

		} else {

			StringBuffer bookNameQuery = new StringBuffer();
			StringBuffer authorQuery = new StringBuffer();

			// extractNameUsingApacheNLP(searchQuery, bookNameQuery,
			// authorQuery);

			StringBuilder capitalizeString = new StringBuilder();
			String[] str = searchQuery.getQuery().split(" ");
			for (int i = 0; i < str.length; i++) {
				capitalizeString.append(str[i].substring(0, 1).toUpperCase() + str[i].substring(1) + " ");
			}
			capitalizeString.deleteCharAt(capitalizeString.length() - 1);
			searchQuery.setQuery(capitalizeString.toString());

			extractNameUsingStanforNLP(searchQuery, bookNameQuery, authorQuery);

			hqlQuery = "from Book b, author a, BookAuthor ba where a.author_id= ba.author.author_id and b.ISBN = ba.book.ISBN and b.title like '%"
					+ bookNameQuery + "%' and a.name like '%" + authorQuery + "%'";

		}

		Query query = session.createQuery(hqlQuery);
		// query.setMaxResults(100);

		List<Book> list = query.list();

		prepareSearchResult(result, list);

		session.close();

		return result;
	}

	/**
	 * Prepares the result
	 * 
	 * @param result
	 * @param list
	 */
	private void prepareSearchResult(List<SearchResult> result, List<Book> list) {
		Book book = new Book();
		Author author = new Author();
		List<Author> authorList = new ArrayList<Author>();

		Map<String, SearchResult> isbn = new HashMap<String, SearchResult>();

		for (Object b : list) {

			Object[] arr = (Object[]) b;

			book = (Book) arr[0];
			author = (Author) arr[1];

			if (isbn.containsKey(book.getISBN())) {
				authorList = isbn.get(book.getISBN()).getAuthor();
				authorList.add(author);

				SearchResult searchRe = new SearchResult();
				searchRe = isbn.get(book.getISBN());
				searchRe.setAuthor(authorList);

				isbn.put(book.getISBN(), searchRe);

			} else {
				SearchResult result2 = new SearchResult();
				result2.setCover(book.getCover());
				result2.setAvailable(book.isAvailable());
				result2.setISBN(book.getISBN());
				result2.setTitle(book.getTitle());
				result2.setPublisher(book.getPublisher());
				result2.setPages(book.getPages());
				List<Author> authors = new ArrayList<Author>();
				authors.add(author);
				result2.setAuthor(authors);
				isbn.put(book.getISBN(), result2);
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
	private void extractNameUsingStanforNLP(SearchQuery searchQuery, StringBuffer bookNameQuery,
			StringBuffer authorQuery) {

		String serializedClassifier = "C://Users/asdha/workspace/DBProject/Library/LibraryManagement/src/main/java/com/library/main/english.all.3class.distsim.crf.ser.gz";

		try {

			if (classifier == null) {
				classifier = CRFClassifier.getClassifier(new File(serializedClassifier));
			}

			int startIndex = 0, endIndex = 0;
			List<Triple<String, Integer, Integer>> triples = classifier
					.classifyToCharacterOffsets(searchQuery.getQuery());
			for (Triple<String, Integer, Integer> trip : triples) {
				startIndex = trip.second();
				endIndex = trip.third();

			}

			authorQuery.append(searchQuery.getQuery().substring(startIndex, endIndex));
			bookNameQuery.append(searchQuery.getQuery().substring(0, startIndex));
			bookNameQuery.append(searchQuery.getQuery().substring(endIndex, searchQuery.getQuery().length()));

			System.out.println("AuthorQuery " + authorQuery);
			System.out.println("Book Query " + bookNameQuery);

		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private void extractNameUsingApacheNLP(SearchQuery searchQuery, StringBuffer bookNameQuery,
			StringBuffer authorQuery) {
		InputStream modelFile = null;

		String fileName = "C://Users/asdha/workspace/DBProject/Library/LibraryManagement/src/main/java/com/library/main/en-ner-person.bin";

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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Book Query " + bookNameQuery);
		System.out.println("NamedQuery " + authorQuery);
	}

	
	public List<Fine> getFineForCardId(SearchQuery searchQuery) {
	
		
		boolean isPaid = Boolean.parseBoolean(searchQuery.getPaid());
		
		System.out.println("Ispaid " + isPaid);
		Session session = this.sessionFactory.openSession();
		String param = "from BookLoan where borrower.cardId="+ Integer.parseInt(searchQuery.getQuery());
				
		Query query = session.createQuery(param);
		
		List<BookLoan> bookLoans = query.list();
		
		List<Fine> fines = new ArrayList<Fine>();
		
		
		for(BookLoan bookLoan :bookLoans){
			if(bookLoan.getFine()!=null){
				if(isPaid && bookLoan.getFine().isPaid()){
					fines.add(bookLoan.getFine());
				}
				if(!isPaid && !bookLoan.getFine().isPaid()){
					fines.add(bookLoan.getFine());
				}
			}			
		}
		
		session.close();
		
		return fines;
	}

}
