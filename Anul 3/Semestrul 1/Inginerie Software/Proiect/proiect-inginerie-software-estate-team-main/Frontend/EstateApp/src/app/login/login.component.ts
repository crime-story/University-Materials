import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from 'src/services/login.service';
import jwt_decode from 'jwt-decode';
import { LoginToken } from 'src/interfaces/LoginToken';
import { MatDialog } from '@angular/material/dialog';
import { LoginSuccessfullyComponent } from './operations/login-successfully/login-successfully.component';
import { LoginUnsuccessfullyComponent } from './operations/login-unsuccessfully/login-unsuccessfully.component';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  hide = true;
  loginForm! : FormGroup;
  waitingLog = false;

  constructor(
    private formBuilder: FormBuilder,
    private loginService : LoginService,
    public dialog : MatDialog
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  async onSubmit() : Promise<any>{
    this.waitingLog = true;
    await new Promise(f => setTimeout(f, 1000));
    this.loginService.Login(this.loginForm.value['email'], this.loginForm.value['password']).subscribe(
      (rez: any) =>  {
        var toReturn = rez as LoginToken
        localStorage.setItem('token', toReturn.token)
        localStorage.setItem('expiration', toReturn.expiration)
        this.dialog.open(LoginSuccessfullyComponent, {
          height: '60px',
          width: '300px',
          position: {
            left: '39vw',
            top: '30px'
          }
        })
        this.waitingLog = false;
      },
      (error : any) => {
        this.dialog.open(LoginUnsuccessfullyComponent, {
          height: '60px',
          width: '300px',
          position: {
            left: '39vw',
            top: '30px'
          }
        })
        this.waitingLog = false;
      });
  }

}
