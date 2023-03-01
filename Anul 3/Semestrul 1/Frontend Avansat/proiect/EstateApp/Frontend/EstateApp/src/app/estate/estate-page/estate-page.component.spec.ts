import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstatePageComponent } from './estate-page.component';

describe('EstatePageComponent', () => {
  let component: EstatePageComponent;
  let fixture: ComponentFixture<EstatePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EstatePageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstatePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
