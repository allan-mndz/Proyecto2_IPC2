import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminComision } from './admin-comision';

describe('AdminComision', () => {
  let component: AdminComision;
  let fixture: ComponentFixture<AdminComision>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminComision],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminComision);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
