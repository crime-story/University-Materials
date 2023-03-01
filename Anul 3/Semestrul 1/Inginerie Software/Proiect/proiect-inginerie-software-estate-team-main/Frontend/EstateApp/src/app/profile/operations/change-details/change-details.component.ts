import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AuthToken } from 'src/interfaces/AuthToken';
import { LoginService } from 'src/services/login.service';
import { UserDetailsService } from 'src/services/userDetails.service';

@Component({
  selector: 'app-change-details',
  templateUrl: './change-details.component.html',
  styleUrls: ['./change-details.component.scss']
})
export class ChangeDetailsComponent implements OnInit{
  username: any;
  hide = true;
  registerForm! : FormGroup;
  checkedValue = false;
  userData: any;
  waitingUpdate = true;
  waitingSuccess = false;

  constructor(
    private formBuilder: FormBuilder,
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
    var token = localStorage.getItem('token');
    if(token){
      this.userService.GetUserDetails(this.username).subscribe(
        async (rez: any) => {
          this.userData = rez
          console.log(this.userData)
        });
    }
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
  changeValue(){
    this.checkedValue = !this.checkedValue
  }
  async onSubmit(){
    this.waitingUpdate = false;
    await new Promise(f => setTimeout(f, 1000));
    console.log(this,this.registerForm.value['profileURL']);
    this.userService.UpdateUserDetails(this.username, this.registerForm.value['age'], this.registerForm.value['fullName'],
      this.registerForm.value['residence'],this.registerForm.value['description'], this.registerForm.value['profileURL']).subscribe(async rez=>{
        this.waitingSuccess = true;
        await new Promise(f => setTimeout(f, 1000));
        window.location.reload();
      })

  }
}

