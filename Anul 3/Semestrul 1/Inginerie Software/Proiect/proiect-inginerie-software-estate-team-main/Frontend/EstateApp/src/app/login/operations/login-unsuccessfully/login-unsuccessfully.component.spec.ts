import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginUnsuccessfullyComponent } from './login-unsuccessfully.component';

describe('LoginUnsuccessfullyComponent', () => {
  let component: LoginUnsuccessfullyComponent;
  let fixture: ComponentFixture<LoginUnsuccessfullyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginUnsuccessfullyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginUnsuccessfullyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
