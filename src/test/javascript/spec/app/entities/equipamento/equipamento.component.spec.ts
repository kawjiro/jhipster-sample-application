/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { EquipamentoComponent } from 'app/entities/equipamento/equipamento.component';
import { EquipamentoService } from 'app/entities/equipamento/equipamento.service';
import { Equipamento } from 'app/shared/model/equipamento.model';

describe('Component Tests', () => {
    describe('Equipamento Management Component', () => {
        let comp: EquipamentoComponent;
        let fixture: ComponentFixture<EquipamentoComponent>;
        let service: EquipamentoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [EquipamentoComponent],
                providers: []
            })
                .overrideTemplate(EquipamentoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(EquipamentoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EquipamentoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Equipamento(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.equipamentos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
