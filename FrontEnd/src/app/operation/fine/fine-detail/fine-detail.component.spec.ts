import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FineDetailComponent } from './fine-detail.component';
import { LibraryService } from 'src/app/services/library.services';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
describe('FineDetailComponent', () => {
  let component: FineDetailComponent;
  let fixture: ComponentFixture<FineDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FineDetailComponent ],
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [LibraryService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FineDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
