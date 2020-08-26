import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenericMonthHitComponent } from './generic-month-hit.component';

describe('GenericMonthHitComponent', () => {
  let component: GenericMonthHitComponent;
  let fixture: ComponentFixture<GenericMonthHitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GenericMonthHitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenericMonthHitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
