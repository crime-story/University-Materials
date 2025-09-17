import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RestaurantsRoutingModule } from './restaurants-routing.module';
import { RestaurantsComponent } from './restaurants/restaurants.component';


@NgModule({
  declarations: [
    RestaurantsComponent
  ],
  imports: [
    CommonModule,
    RestaurantsRoutingModule
  ]
})
export class RestaurantsModule { }
