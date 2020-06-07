import { NgModule } from '@angular/core';
import { FineComponent } from './fine/fine.component';
import { BorrowerComponent } from './borrower/borrower.component';
import { SharedModule } from '../shared/shared.module';
import { FineDetailComponent } from './fine/fine-detail/fine-detail.component';
import { RouterModule } from '@angular/router';
import { CalculateFineComponent } from './fine/calculate-fine/calculate-fine.component';
import { PayFineComponent } from './fine/pay-fine/pay-fine.component';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [FineComponent, BorrowerComponent, FineDetailComponent, CalculateFineComponent, PayFineComponent]
})

export class OperationModule { }
