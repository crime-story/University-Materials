import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { LoginToken } from 'src/interfaces/LoginToken';
import { LoginService } from 'src/services/login.service';
import { UserDetailsService } from 'src/services/userDetails.service';
import { TermsComponent } from '../login/terms/terms.component';
import { AccountSuccessfullyComponent } from './account-successfully/account-successfully.component';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})

export class RegisterComponent implements OnInit {
 
  hide = true;
  registerForm! : FormGroup;
  waitingLog = false;
  checkedValue = false;
  hasImage=false;

  constructor(
    private formBuilder: FormBuilder,
    private loginService : LoginService,
    private userService : UserDetailsService,
    public dialog: MatDialog
  ) {} 
  formatLabel(value: number): string {
    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }

    return `${value}`;
  }
  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      email: ['',[Validators.required]],
      password: ['', [Validators.required, Validators.minLength(5)]],
      role: ['', [Validators.required]],
      fullName: ['', [Validators.required]],
      residence: ['', [Validators.required]],
      profileURL: ['', [Validators.required]],
      description: ['', [Validators.required]],
      age: ['', [Validators.required]]
    });
  }

  openTerms($event:any) : any{
    console.log($event)
    this.dialog.open(TermsComponent, {
      height: '60%',
      width: '43%',
    });
  }
  changeValue(){
    this.checkedValue = !this.checkedValue
  }
  async onSubmit(){
    this.waitingLog = true;
    var image = document.getElementById('profileImage');
    var url : any;
    if(image && image.getAttribute('src')){
      url = image.getAttribute('src');
    }
    else{
      url = 'default'
    }
    await new Promise(f => setTimeout(f, 1000));
    this.loginService.Auth(this.registerForm.value['email'], this.registerForm.value['password'], this.registerForm.value['role']).subscribe(
      (rez: any) =>  {
      },
      (error : any) => {
        this.userService.CreateUserDetails(this.registerForm.value['fullName'],this.registerForm.value['email']
        ,this.registerForm.value['age'],this.registerForm.value['residence'],this.registerForm.value['description'],url).subscribe(
          (rez: any) => {
            this.dialog.open(AccountSuccessfullyComponent, {
              height: '60px',
              width: '300px',
              position: {
                left: '39vw',
                top: '30px'
              }
            })
            this.waitingLog = false;
          }
        )
      });
  }

  profileImage($event : any){
    this.hasImage = true;
    var file = new FileReader();
    file.readAsDataURL($event.target.files[0])
    file.onload = (event:any) => {
      var url = event.target.result;
      if(url){
        var image = document.getElementById('profileImage');
        console.log(image);
        if(image){
          image.setAttribute('src', url);
        }
      }
    }
  }
}
