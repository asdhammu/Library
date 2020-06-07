import { Component, OnInit, Input, Output } from '@angular/core';
import { Book } from 'src/app/model/Book';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit {

  @Input() books: Book[];
  @Input() isCheckIn: boolean;

  @Output() checkOut = new EventEmitter();
  @Output() checkIn = new EventEmitter();
  constructor() { }

  ngOnInit() {
  }

  checkInEmit(isbn, cardId) {
    const obj = new CheckInEvent();
    obj.cardId = cardId;
    obj.isbn = isbn;
    this.checkIn.emit(obj);
  }

  checkOutEmit(isbn) {
    this.checkOut.emit(isbn);
  }
}

class CheckInEvent {
  cardId: number;
  isbn: string;
}
