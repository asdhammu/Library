import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PayFineComponent } from './pay-fine.component';

describe('PayFineComponent', () => {
  let component: PayFineComponent;
  let fixture: ComponentFixture<PayFineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PayFineComponent ]
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
