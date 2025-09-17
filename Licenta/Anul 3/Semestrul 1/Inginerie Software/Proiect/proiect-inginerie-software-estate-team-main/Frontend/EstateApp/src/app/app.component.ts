import { ElementRef, Renderer2 } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'EstateApp';
  constructor(private el: ElementRef, private renderer:Renderer2){}

  ngAfterViewInit(){

  this.renderer.setStyle(this.el.nativeElement.ownerDocument.body,'background-image', 'url(https://wallpaperaccess.com/full/1950954.jpg)');
  this.renderer.setStyle(this.el.nativeElement.ownerDocument.body,'background-size', 'cover');

}
}
