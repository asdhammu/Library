import { Routes } from '@angular/router';
import { SearchCheckInBookComponent } from './search/check-in/searchCheckInBook.component';
import { SearchComponent } from './search/search.component';
import { CheckOutBookComponent } from './user/check-out/checkOutBook.component';
import { BorrowerComponent } from './operation/borrower/borrower.component';
import { CheckInBookComponent } from './user/check-in/CheckInBook.component';
import { FineComponent } from './operation/fine/fine.component';
import { FineDetailComponent } from './operation/fine/fine-detail/fine-detail.component';
import { CalculateFineComponent } from './operation/fine/calculate-fine/calculate-fine.component';
import { PayFineComponent } from './operation/fine/pay-fine/pay-fine.component';

export const appRoutes: Routes = [
  { path: 'addBorrower', component: BorrowerComponent },
  { path: 'search', component: SearchComponent },
  { path: 'addLoan/:isbn', component: CheckOutBookComponent },
  { path: 'checkIn', component: SearchCheckInBookComponent },
  { path: 'addCheckIn/:isbn/:borrowerId', component: CheckInBookComponent },
  {
    path: 'fine', component: FineComponent, children: [
      {
        path: 'list',
        component: FineDetailComponent
      },
      {
        path: 'calculate',
        component: CalculateFineComponent
      },
      {
        path: 'pay/:cardId',
        component: PayFineComponent
      }
    ]
  }
];
