import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstatesListComponent } from './estates-list.component';

describe('EstatesListComponent', () => {
  let component: EstatesListComponent;
  let fixture: ComponentFixture<EstatesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EstatesListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstatesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
