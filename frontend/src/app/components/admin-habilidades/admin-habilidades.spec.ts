import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminHabilidades } from './admin-habilidades';

describe('AdminHabilidades', () => {
  let component: AdminHabilidades;
  let fixture: ComponentFixture<AdminHabilidades>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminHabilidades],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminHabilidades);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
