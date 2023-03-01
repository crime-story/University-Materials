import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import jwt_decode from 'jwt-decode';
import { AuthToken } from 'src/interfaces/AuthToken';
import { LoginService } from 'src/services/login.service';
import { UserDetailsService } from 'src/services/userDetails.service';

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss']
})
export class DeleteComponent implements OnInit{

  deleteAccountForm!: FormGroup
  checkedValue = true;
  deleteSuccess = false;
  waitDelete = false;
  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private userDetailsService: UserDetailsService
  ){}
  ngOnInit(): void {
    this.deleteAccountForm = this.formBuilder.group({
      password: ['',[Validators.required]]
    })
  }

  async onSubmit(){
    this.waitDelete = true;
    await new Promise(f => setTimeout(f, 1000));
    var token = localStorage.getItem('token')
    if(token){
      var decode = jwt_decode<AuthToken>(token.toString())
      this.loginService.DeleteAccount(decode["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"], 
        this.deleteAccountForm.value['password']).subscribe(
          async (rez : any) => {
            this.userDetailsService.DeleteUserDetails(decode["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"])
            .subscribe(
              async(rez : any) =>{
                this.deleteSuccess = true;
                await new Promise(f => setTimeout(f, 1000));
                window.location.href = 'http://localhost:4200/login';
              }
            )
          },
          (error : any) => {
            console.log(error);
          }
        );
    }
    this.waitDelete = false;
  }
  changeValue(){
    this.checkedValue = !this.checkedValue;
  }
}
