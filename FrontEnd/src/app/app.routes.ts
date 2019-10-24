import {Routes} from "@angular/router";
import {SearchCheckInBookComponent} from "./search/check-in/searchCheckInBook.component";
import {SearchComponent} from "./search/search.component";
import {CheckOutBookComponent} from "./user/check-out/checkOutBook.component";
import {PayComponent} from "./operation/pay/pay.component";
import {BorrowerComponent} from "./operation/borrower/borrower.component";
import {CheckInBookComponent} from "./user/check-in/CheckInBook.component";
import {FineComponent} from "./operation/fine/fine.component";

export const appRoutes: Routes = [
  { path: 'addBorrower', component:  BorrowerComponent },
  { path:'search', component: SearchComponent},
  { path: 'addLoan/:isbn', component: CheckOutBookComponent },
  {path:'checkIn', component:SearchCheckInBookComponent},
  {path:'addCheckIn/:isbn/:borrowerId', component:CheckInBookComponent},
  {path:'fine', component: FineComponent},
  {path:'pay/:cardId',component:PayComponent}
];
