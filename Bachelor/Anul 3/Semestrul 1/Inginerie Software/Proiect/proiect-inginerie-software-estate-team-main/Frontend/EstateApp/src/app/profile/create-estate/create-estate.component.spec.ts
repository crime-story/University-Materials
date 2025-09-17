import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEstateComponent } from './create-estate.component';

describe('CreateEstateComponent', () => {
  let component: CreateEstateComponent;
  let fixture: ComponentFixture<CreateEstateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateEstateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEstateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
