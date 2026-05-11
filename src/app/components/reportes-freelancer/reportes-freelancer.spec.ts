import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportesFreelancer } from './reportes-freelancer';

describe('ReportesFreelancer', () => {
  let component: ReportesFreelancer;
  let fixture: ComponentFixture<ReportesFreelancer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportesFreelancer],
    }).compileComponents();

    fixture = TestBed.createComponent(ReportesFreelancer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
