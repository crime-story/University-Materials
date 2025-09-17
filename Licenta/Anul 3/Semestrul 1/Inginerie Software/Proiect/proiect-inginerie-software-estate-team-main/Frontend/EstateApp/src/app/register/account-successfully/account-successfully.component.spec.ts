import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountSuccessfullyComponent } from './account-successfully.component';

describe('AccountSuccessfullyComponent', () => {
  let component: AccountSuccessfullyComponent;
  let fixture: ComponentFixture<AccountSuccessfullyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccountSuccessfullyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountSuccessfullyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
