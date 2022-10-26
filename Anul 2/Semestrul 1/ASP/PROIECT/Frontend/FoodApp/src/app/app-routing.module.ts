import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';

const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        loadChildren: () => import('src/app/modules/locations/locations.module').then(m => m.LocationsModule),
      },
      {
        path: '',
        loadChildren: () => import('src/app/modules/restaurants/restaurants.module').then(m => m.RestaurantsModule),
      },
      {
        path: '',
        loadChildren: () => import('src/app/modules/products/products.module').then(m => m.ProductsModule),
      },
    ]
  },
  {
    path: 'auth',
    loadChildren: () =>import('src/app/modules/auth/auth.module').then( m=> m.AuthModule),
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
