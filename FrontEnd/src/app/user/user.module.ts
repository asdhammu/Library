import {NgModule} from "@angular/core";
import {CheckInBookComponent} from "./check-in/CheckInBook.component";
import {CheckOutBookComponent} from "./check-out/checkOutBook.component";
import {SharedModule} from "../shared/shared.module";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  imports: [SharedModule, MatInputModule, MatButtonModule],
  declarations: [CheckInBookComponent, CheckOutBookComponent]
})

export class UserModule {}
