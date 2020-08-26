import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IbanSelectorComponent } from './iban-selector.component';

describe('IbanSelectorComponent', () => {
  let component: IbanSelectorComponent;
  let fixture: ComponentFixture<IbanSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IbanSelectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IbanSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
