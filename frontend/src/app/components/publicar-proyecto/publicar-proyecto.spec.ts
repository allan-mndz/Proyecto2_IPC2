import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicarProyecto } from './publicar-proyecto';

describe('PublicarProyecto', () => {
  let component: PublicarProyecto;
  let fixture: ComponentFixture<PublicarProyecto>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicarProyecto],
    }).compileComponents();

    fixture = TestBed.createComponent(PublicarProyecto);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
