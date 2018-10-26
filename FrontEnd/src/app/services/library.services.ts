import {Injectable}     from '@angular/core';
import {Http,Headers, RequestOptions} from '@angular/http';
import 'rxjs/Rx';
import {Borrower} from '../Model/Borrower';
// Import RxJs required methods
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {BookLoan} from "../Model/BookLoan";
import {CheckInBookModel} from "../Model/CheckInBookModel";

@Injectable()
export class LibraryService {

  constructor(private http: Http) {
  }

  private apiURL = "http://localhost:8080/LibraryManagement/";

  addBorrower(borrower: Borrower): Promise<any> {    
    let test_this = {
      "name": borrower.name,
      "ssn": borrower.ssn,
      "address": borrower.address,
      "phone": borrower.phone
    };


    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});    
    return this.http.post(this.apiURL + "addBorrower", JSON.stringify(test_this), options).toPromise()
      .then(response => response.json())
      .catch(this.handleError);
  }

  search(searchQuery: string): Promise<any> {

    let data = {"query": searchQuery};
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.post(this.apiURL + "search", JSON.stringify(data), options).toPromise()
      .then(response => response.json())
      .catch(this.handleError);

  }

  addLoan(bookLoan: BookLoan): Promise<any> {

    let data = {"isbn": bookLoan.isbn, "borrowerId": bookLoan.borrowerId};
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.post(this.apiURL + "checkoutBook", JSON.stringify(data), options).toPromise()
      .then(response => response.json())
      .catch(this.handleError);

  }

  addOrUpdateFine():Promise<any>{

    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.get(this.apiURL + "addOrUpdateFine", options).toPromise()
      .then(response =>response.json())
      .catch(this.handleError);

  }

  searchToCheckIn(checkIn:CheckInBookModel):Promise<any>{

    let data = {"isbn":checkIn.isbn,"name":checkIn.name,"cardId":checkIn.cardId};


    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.post(this.apiURL + "searchCheckedInBooks", JSON.stringify(data), options).toPromise()
      .then(response =>response.json())
      .catch(this.handleError);

  }


  checkIn(isbn:string,cardId:string):Promise<any>{

    let data = {"isbn":isbn,"cardId":cardId};

    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.post(this.apiURL + "checkInBook", JSON.stringify(data), options).toPromise()
      .then(response =>response.json())
      .catch(this.handleError);

  }

  showFines():Promise<any>{    
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.get(this.apiURL + "getAllFines", options).toPromise()
      .then(response =>response.json())
      .catch(this.handleError);
  }

  payFine(cardId:number):Promise<any>{
    let data = {"query":cardId};
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.post(this.apiURL + "payFine", JSON.stringify(data), options).toPromise()
      .then(response =>response.json())
      .catch(this.handleError);
  }

  getFineForCardId(cardId:number, paid:string):Promise<any>{
    let data = {"query":cardId,"paid":paid};
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.post(this.apiURL + "getFineForCardId", JSON.stringify(data), options).toPromise()
      .then(response =>response.json())
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error("error occurred", error);
    return Promise.reject(error.message || error);
  }


}
