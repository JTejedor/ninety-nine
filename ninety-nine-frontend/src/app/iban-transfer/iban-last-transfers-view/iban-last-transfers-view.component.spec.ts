import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IbanLastTransfersViewComponent } from './iban-last-transfers-view.component';

describe('IbanLastTransfersViewComponent', () => {
  let component: IbanLastTransfersViewComponent;
  let fixture: ComponentFixture<IbanLastTransfersViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IbanLastTransfersViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IbanLastTransfersViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
