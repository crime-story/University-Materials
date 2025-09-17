import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login-successfully',
  templateUrl: './login-successfully.component.html',
  styleUrls: ['./login-successfully.component.scss']
})
export class LoginSuccessfullyComponent implements OnInit {
  async ngOnInit(): Promise<void> {
    await new Promise(f => setTimeout(f, 1000));
    window.location.href = 'http://localhost:4200/profile'
  }
}
