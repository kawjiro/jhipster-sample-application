/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ModeloEquipamentoUpdateComponent } from 'app/entities/modelo-equipamento/modelo-equipamento-update.component';
import { ModeloEquipamentoService } from 'app/entities/modelo-equipamento/modelo-equipamento.service';
import { ModeloEquipamento } from 'app/shared/model/modelo-equipamento.model';

describe('Component Tests', () => {
    describe('ModeloEquipamento Management Update Component', () => {
        let comp: ModeloEquipamentoUpdateComponent;
        let fixture: ComponentFixture<ModeloEquipamentoUpdateComponent>;
        let service: ModeloEquipamentoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ModeloEquipamentoUpdateComponent]
            })
                .overrideTemplate(ModeloEquipamentoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ModeloEquipamentoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ModeloEquipamentoService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ModeloEquipamento(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.modeloEquipamento = entity;
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
                    const entity = new ModeloEquipamento();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.modeloEquipamento = entity;
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
