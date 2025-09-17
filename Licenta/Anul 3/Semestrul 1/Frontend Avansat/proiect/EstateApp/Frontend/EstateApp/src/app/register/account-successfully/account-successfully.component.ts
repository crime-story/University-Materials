import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-account-successfully',
  templateUrl: './account-successfully.component.html',
  styleUrls: ['./account-successfully.component.scss']
})
export class AccountSuccessfullyComponent implements OnInit {
  async ngOnInit(): Promise<void> {
    await new Promise(f => setTimeout(f, 1000));
    window.location.href = 'http://localhost:4200/login'
  }

}
