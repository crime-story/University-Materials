import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { EstatePageComponent } from './estate/estate-page/estate-page.component';
import { EstatesListComponent } from './estate/estates-list/estates-list.component';
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
  { path : "login", component: LoginComponent},
  { path : "register", component: RegisterComponent},
  { path : "estate/:id", component: EstatePageComponent },
  { path : "profile", component: ProfileComponent, canActivate: [AuthGuard] },
  { path: "estates/:pageNumber", component: EstatesListComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
