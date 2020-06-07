import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CalculateFineComponent } from './calculate-fine.component';

describe('CalculateFineComponent', () => {
  let component: CalculateFineComponent;
  let fixture: ComponentFixture<CalculateFineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CalculateFineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CalculateFineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
