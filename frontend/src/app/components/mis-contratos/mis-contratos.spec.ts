import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisContratos } from './mis-contratos';

describe('MisContratos', () => {
  let component: MisContratos;
  let fixture: ComponentFixture<MisContratos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisContratos],
    }).compileComponents();

    fixture = TestBed.createComponent(MisContratos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
