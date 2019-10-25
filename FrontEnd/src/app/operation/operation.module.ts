import {NgModule} from "@angular/core";
import {PayComponent} from "./pay/pay.component";
import {FineComponent} from "./fine/fine.component";
import {BorrowerComponent} from "./borrower/borrower.component";
import {SharedModule} from "../shared/shared.module";

@NgModule({
  imports: [SharedModule],
  declarations : [PayComponent, FineComponent, BorrowerComponent]
})

export class OperationModule {}
