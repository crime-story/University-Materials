import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewYourEstatesComponent } from './view-your-estates.component';

describe('ViewYourEstatesComponent', () => {
  let component: ViewYourEstatesComponent;
  let fixture: ComponentFixture<ViewYourEstatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewYourEstatesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewYourEstatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
