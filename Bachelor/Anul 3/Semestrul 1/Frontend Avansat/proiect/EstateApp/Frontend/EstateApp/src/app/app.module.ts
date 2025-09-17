import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from 
    '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatIconModule } from '@angular/material/icon'
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input'
import { MatButtonModule } from '@angular/material/button'
import { MatToolbarModule } from '@angular/material/toolbar';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatGridListModule } from '@angular/material/grid-list';
import { HttpClientModule } from '@angular/common/http';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { RegisterComponent } from './register/register.component';
import { MatRadioModule } from '@angular/material/radio';
import { MatCheckboxModule } from '@angular/material/checkbox'
import { MatDialogModule } from '@angular/material/dialog';
import { TermsComponent } from './login/terms/terms.component';
import { AccountSuccessfullyComponent } from './register/account-successfully/account-successfully.component';
import { LoginSuccessfullyComponent } from './login/operations/login-successfully/login-successfully.component';
import { LoginUnsuccessfullyComponent } from './login/operations/login-unsuccessfully/login-unsuccessfully.component';
import { ProfileComponent } from './profile/profile.component';
import { HomePageComponent } from './home-page/home-page.component';
import { MatSliderModule } from '@angular/material/slider';
import { MenuComponent } from './menu/menu.component';
import { LogoutComponent } from './profile/operations/logout/logout.component';
import { ChangeDetailsComponent } from './profile/operations/change-details/change-details.component';
import { DeleteComponent } from './profile/operations/delete/delete.component';
import { CreateEstateComponent } from './profile/create-estate/create-estate.component';
import { MatButton } from '@angular/material/button';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { ViewYourEstatesComponent } from './profile/view-your-estates/view-your-estates.component';
import { EstatePageComponent } from './estate/estate-page/estate-page.component';
import { EstatesListComponent } from './estate/estates-list/estates-list.component';
import { initializeApp,provideFirebaseApp } from '@angular/fire/app';
import { environment } from '../environments/environment';
import { provideDatabase,getDatabase } from '@angular/fire/database';
import { provideFirestore,getFirestore } from '@angular/fire/firestore';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    TermsComponent,
    AccountSuccessfullyComponent,
    LoginSuccessfullyComponent,
    LoginUnsuccessfullyComponent,
    ProfileComponent,
    HomePageComponent,
    MenuComponent,
    LogoutComponent,
    ChangeDetailsComponent,
    DeleteComponent,
    CreateEstateComponent,
    ViewYourEstatesComponent,
    EstatePageComponent,
    EstatesListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatButtonModule,
    MatToolbarModule,
    MatGridListModule,
    MatIconModule,
    DragDropModule,
    HttpClientModule,
    MatProgressBarModule,
    MatRadioModule,
    MatCheckboxModule,
    MatDialogModule,
    MatSliderModule,
    MatButtonToggleModule,
    provideFirebaseApp(() => initializeApp(environment.firebase)),
    provideDatabase(() => getDatabase()),
    provideFirestore(() => getFirestore())
  ],
  exports: [
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatButtonModule,
    MatToolbarModule,
    MatGridListModule,
    MatIconModule,
    DragDropModule,
    MatButton,
    MatButtonToggleModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
