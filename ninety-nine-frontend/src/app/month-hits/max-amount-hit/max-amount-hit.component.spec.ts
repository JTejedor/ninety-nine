import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MaxAmountHitComponent } from './max-amount-hit.component';

describe('MaxAmountHitComponent', () => {
  let component: MaxAmountHitComponent;
  let fixture: ComponentFixture<MaxAmountHitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MaxAmountHitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MaxAmountHitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
