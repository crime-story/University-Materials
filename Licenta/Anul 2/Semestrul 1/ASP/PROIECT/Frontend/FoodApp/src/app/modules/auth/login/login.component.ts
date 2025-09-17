import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AbstractControl, FormControl, FormGroup } from "@angular/forms";
import { DataService } from '../../../services/data.service';
// import { AuthService } from '../../../services/loginserver.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });
  
  constructor(
    private router: Router,
    private dataService: DataService,
  ) { }

  // getters
  get username(): AbstractControl | null {
    return this.loginForm.get('username');
  }

  get password(): AbstractControl | null {
    return this.loginForm.get('password');
  }

  ngOnInit(): void {
  }

  public login(): void {
    console.log(this.loginForm.value);
    this.dataService.changeDataUser(this.loginForm.value);
    localStorage.setItem('Role', 'Admin');
    this.router.navigate(['/locations']);
  }

  public signup(): void {
    this.router.navigate(['auth/signup']);
  }
}
