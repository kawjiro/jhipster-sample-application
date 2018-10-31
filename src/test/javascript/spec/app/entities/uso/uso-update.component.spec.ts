/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { UsoUpdateComponent } from 'app/entities/uso/uso-update.component';
import { UsoService } from 'app/entities/uso/uso.service';
import { Uso } from 'app/shared/model/uso.model';

describe('Component Tests', () => {
    describe('Uso Management Update Component', () => {
        let comp: UsoUpdateComponent;
        let fixture: ComponentFixture<UsoUpdateComponent>;
        let service: UsoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [UsoUpdateComponent]
            })
                .overrideTemplate(UsoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UsoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UsoService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Uso(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.uso = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Uso();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.uso = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
