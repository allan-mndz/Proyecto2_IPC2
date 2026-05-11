import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuscarTrabajos } from './buscar-trabajos';

describe('BuscarTrabajos', () => {
  let component: BuscarTrabajos;
  let fixture: ComponentFixture<BuscarTrabajos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuscarTrabajos],
    }).compileComponents();

    fixture = TestBed.createComponent(BuscarTrabajos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
