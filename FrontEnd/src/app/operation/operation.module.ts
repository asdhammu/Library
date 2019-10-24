import {NgModule} from "@angular/core";
import {PayComponent} from "./pay/pay.component";
import {FineComponent} from "./fine/fine.component";
import {BorrowerComponent} from "./borrower/borrower.component";

@NgModule({
  imports: [],
  exports : [PayComponent, FineComponent, BorrowerComponent]
})

export class OperationModule {}
