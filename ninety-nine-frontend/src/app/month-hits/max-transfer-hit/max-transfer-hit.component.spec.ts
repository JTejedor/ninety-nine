import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MaxTransferHitComponent } from './max-transfer-hit.component';

describe('MaxTranferHitComponent', () => {
  let component: MaxTransferHitComponent;
  let fixture: ComponentFixture<MaxTransferHitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MaxTransferHitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MaxTransferHitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
