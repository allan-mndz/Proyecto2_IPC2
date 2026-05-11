import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuSuperior } from './menu-superior';

describe('MenuSuperior', () => {
  let component: MenuSuperior;
  let fixture: ComponentFixture<MenuSuperior>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MenuSuperior],
    }).compileComponents();

    fixture = TestBed.createComponent(MenuSuperior);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
