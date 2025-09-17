import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { DataService } from 'src/app/services/data.service';
import { LoginserverService } from 'src/app/services/loginserver.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit, OnDestroy  {
  public message: any;
  public subscription!: Subscription;

  public signupForm: FormGroup = new FormGroup({
    email: new FormControl(''),
    username: new FormControl(''),
    password: new FormControl(''),
    verifyPassword: new FormControl(''),
  });

  constructor(
    private router: Router,
    private dataService: DataService,
    private signupService: LoginserverService
  ) {}

  // getters
  get email(): AbstractControl | null
  {
    return this.signupForm.get('email');
  }

  get username(): AbstractControl | null
  {
    return this.signupForm.get('username');
  }

  get password(): AbstractControl | null
  {
    return this.signupForm.get('password');
  }

  get verifyPassword(): AbstractControl | null
  {
    return this.signupForm.get('verifyPassword');
  }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public signup(): void {
    this.signupService.signup(this.signupForm.value).subscribe(
      (result) => {
        alert('Success on signup!');
        this.router.navigate(['/login']);
      },
      (error) => {
        alert('Error on signup!');
      }
    );
  }
}
