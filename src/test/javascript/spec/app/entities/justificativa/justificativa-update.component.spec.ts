/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { JustificativaUpdateComponent } from 'app/entities/justificativa/justificativa-update.component';
import { JustificativaService } from 'app/entities/justificativa/justificativa.service';
import { Justificativa } from 'app/shared/model/justificativa.model';

describe('Component Tests', () => {
    describe('Justificativa Management Update Component', () => {
        let comp: JustificativaUpdateComponent;
        let fixture: ComponentFixture<JustificativaUpdateComponent>;
        let service: JustificativaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [JustificativaUpdateComponent]
            })
                .overrideTemplate(JustificativaUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(JustificativaUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JustificativaService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Justificativa(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.justificativa = entity;
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
                    const entity = new Justificativa();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.justificativa = entity;
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
