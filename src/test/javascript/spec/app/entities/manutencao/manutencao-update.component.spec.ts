/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ManutencaoUpdateComponent } from 'app/entities/manutencao/manutencao-update.component';
import { ManutencaoService } from 'app/entities/manutencao/manutencao.service';
import { Manutencao } from 'app/shared/model/manutencao.model';

describe('Component Tests', () => {
    describe('Manutencao Management Update Component', () => {
        let comp: ManutencaoUpdateComponent;
        let fixture: ComponentFixture<ManutencaoUpdateComponent>;
        let service: ManutencaoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ManutencaoUpdateComponent]
            })
                .overrideTemplate(ManutencaoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ManutencaoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ManutencaoService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Manutencao(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.manutencao = entity;
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
                    const entity = new Manutencao();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.manutencao = entity;
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
