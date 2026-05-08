import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportesCliente } from './reportes-cliente';

describe('ReportesCliente', () => {
  let component: ReportesCliente;
  let fixture: ComponentFixture<ReportesCliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportesCliente],
    }).compileComponents();

    fixture = TestBed.createComponent(ReportesCliente);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
