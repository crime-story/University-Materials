import { Component } from '@angular/core';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {
  redirectHome(){
    window.location.href = "http://localhost:4200/estates/1"
  }
  redirectProfile(){
    window.location.href = "http://localhost:4200/profile"
  }
}
