import { Component, OnInit } from '@angular/core';
import { Borrower } from '../../model/borrower';
import { LibraryService } from '../../services/library.services';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
@Component({
  selector: 'borrower-component',
  templateUrl: './borrower.component.html',
  styleUrls: ['./borrower.component.css']
})

export class BorrowerComponent implements OnInit {

  borrowerForm: FormGroup;
  borrower: Borrower;
  msg: string;
  errorMsg: string;
  submitted = false;
  success = false;
  constructor(
    private libraryService: LibraryService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.borrowerForm = this.formBuilder.group({
      name: ['', Validators.required],
      ssn: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', Validators.required]
    });
  }

  addBorrower() {
    this.submitted = false;
    if (!this.borrowerForm.valid) {
      return;
    }

    let b = new Borrower();
    b.name = this.borrowerForm.value.name;
    b.address = this.borrowerForm.value.address;
    b.phone = this.borrowerForm.value.phone;
    b.ssn = this.borrowerForm.value.ssn;

    this.libraryService.addBorrower(b).subscribe(x => {
      this.success = true;
      this.submitted = true;
      this.msg = 'Added succesfully';
    }, e => {
      this.success = false;
      this.submitted = true;
      this.errorMsg = e.error.message;
    });
  }
}
