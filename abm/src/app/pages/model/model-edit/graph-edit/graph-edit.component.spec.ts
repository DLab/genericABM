/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { GraphEditComponent } from './graph-edit.component';

describe('GraphEditComponent', () => {
  let component: GraphEditComponent;
  let fixture: ComponentFixture<GraphEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GraphEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GraphEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
