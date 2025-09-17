import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ChangeDetailsComponent } from './operations/change-details/change-details.component';
import { DeleteComponent } from './operations/delete/delete.component';
import { LogoutComponent } from './operations/logout/logout.component';
import jwt_decode from 'jwt-decode';
import { AuthToken } from 'src/interfaces/AuthToken';
import { UserDetailsService } from 'src/services/userDetails.service';
import { CreateEstateComponent } from './create-estate/create-estate.component';
import { ViewYourEstatesComponent } from './view-your-estates/view-your-estates.component';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  userData: any;
  waitLogout = false;
  user: any;
  role: any;
  constructor(
    public dialog: MatDialog,
    private userDetailsService: UserDetailsService
  ){}
  ngOnInit(): void {
    var token = localStorage.getItem('token');
    if(token){
      var decode = jwt_decode<AuthToken>(token.toString())
      this.user = decode["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"]
      this.role = decode["http://schemas.microsoft.com/ws/2008/06/identity/claims/role"]
      console.log(this.role)
      this.userDetailsService.GetUserDetails(this.user).subscribe(
        async (rez: any) => {
          this.userData = rez
        }
      )
    }
  }

  async logout(): Promise<any>{
    this.waitLogout = true;
    await new Promise(f => setTimeout(f, 1000));
    localStorage.setItem('token','');
    localStorage.setItem('expiration', '');
    this.dialog.open(LogoutComponent, {
      height: '60px',
      width: '300px',
      position: {
        left: '39vw',
        top: '30px'
      }
    })
    this.waitLogout = false;
    await new Promise(f => setTimeout(f, 1000));
    window.location.href = 'http://localhost:4200/login'
  }

  edit(): any{
    var popup = this.dialog.open(ChangeDetailsComponent,{
      height: '80vh',
      width: '40vw',
      position: {
        left: '30vw',
        top: '40px',
        bottom: '30px'
      }
    })
    popup.componentInstance['username'] = this.user;
  }

  deleteAccount(){
    this.dialog.open(DeleteComponent,{
      height: '40vh',
      width: '40vw',
      position: {
        left: '30vw',
        top: '200px',
        bottom: '30px'
      }
    });
  }

  createNewEstate(){
    this.dialog.open(CreateEstateComponent, {
      height: '80vh',
      width: '50vw',
      position: {
        left: '25vw',
        top: '50px',
        bottom: '30px'
      }
    })
  }
  viewYourEstates(){
    this.dialog.open(ViewYourEstatesComponent, {
      height: '80vh',
      width: '50vw',
      position: {
        left: '25vw',
        top: '50px',
        bottom: '30px'
      }
    })   
  }
}
