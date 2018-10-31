/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { EquipamentoUpdateComponent } from 'app/entities/equipamento/equipamento-update.component';
import { EquipamentoService } from 'app/entities/equipamento/equipamento.service';
import { Equipamento } from 'app/shared/model/equipamento.model';

describe('Component Tests', () => {
    describe('Equipamento Management Update Component', () => {
        let comp: EquipamentoUpdateComponent;
        let fixture: ComponentFixture<EquipamentoUpdateComponent>;
        let service: EquipamentoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [EquipamentoUpdateComponent]
            })
                .overrideTemplate(EquipamentoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(EquipamentoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EquipamentoService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Equipamento(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.equipamento = entity;
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
                    const entity = new Equipamento();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.equipamento = entity;
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
