import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginSuccessfullyComponent } from './login-successfully.component';

describe('LoginSuccessfullyComponent', () => {
  let component: LoginSuccessfullyComponent;
  let fixture: ComponentFixture<LoginSuccessfullyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginSuccessfullyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginSuccessfullyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
