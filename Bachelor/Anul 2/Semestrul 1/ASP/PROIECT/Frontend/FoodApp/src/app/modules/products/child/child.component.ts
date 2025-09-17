import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-child',
  templateUrl: './child.component.html',
  styleUrls: ['./child.component.scss']
})
export class ChildComponent implements OnInit {
  @Input() messageFromParent: any;
  @Output() messageFromChild = new EventEmitter<string>();
  
  constructor() { }

  ngOnInit(): void {
    console.log(this.messageFromParent);
  }
  emitData() {
    this.messageFromChild.emit('message from child');
  }
}
