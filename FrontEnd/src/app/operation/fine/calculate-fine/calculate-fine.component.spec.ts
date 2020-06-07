import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CalculateFineComponent } from './calculate-fine.component';
import { LibraryService } from 'src/app/services/library.services';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

describe('CalculateFineComponent', () => {
  let component: CalculateFineComponent;
  let fixture: ComponentFixture<CalculateFineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CalculateFineComponent ],
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [LibraryService]
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
