import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeDetailsComponent } from './change-details.component';

describe('ChangeDetailsComponent', () => {
  let component: ChangeDetailsComponent;
  let fixture: ComponentFixture<ChangeDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChangeDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangeDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
