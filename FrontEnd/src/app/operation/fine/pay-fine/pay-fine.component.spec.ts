import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PayFineComponent } from './pay-fine.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { LibraryService } from 'src/app/services/library.services';
describe('PayFineComponent', () => {
  let component: PayFineComponent;
  let fixture: ComponentFixture<PayFineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PayFineComponent ],
      imports: [HttpClientTestingModule, RouterTestingModule],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      providers: [LibraryService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PayFineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
