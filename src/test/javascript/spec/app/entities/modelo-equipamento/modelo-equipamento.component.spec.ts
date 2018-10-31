/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ModeloEquipamentoComponent } from 'app/entities/modelo-equipamento/modelo-equipamento.component';
import { ModeloEquipamentoService } from 'app/entities/modelo-equipamento/modelo-equipamento.service';
import { ModeloEquipamento } from 'app/shared/model/modelo-equipamento.model';

describe('Component Tests', () => {
    describe('ModeloEquipamento Management Component', () => {
        let comp: ModeloEquipamentoComponent;
        let fixture: ComponentFixture<ModeloEquipamentoComponent>;
        let service: ModeloEquipamentoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ModeloEquipamentoComponent],
                providers: []
            })
                .overrideTemplate(ModeloEquipamentoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ModeloEquipamentoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ModeloEquipamentoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ModeloEquipamento(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.modeloEquipamentos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
