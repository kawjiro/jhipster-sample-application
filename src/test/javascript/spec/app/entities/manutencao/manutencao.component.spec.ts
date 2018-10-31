/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ManutencaoComponent } from 'app/entities/manutencao/manutencao.component';
import { ManutencaoService } from 'app/entities/manutencao/manutencao.service';
import { Manutencao } from 'app/shared/model/manutencao.model';

describe('Component Tests', () => {
    describe('Manutencao Management Component', () => {
        let comp: ManutencaoComponent;
        let fixture: ComponentFixture<ManutencaoComponent>;
        let service: ManutencaoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ManutencaoComponent],
                providers: []
            })
                .overrideTemplate(ManutencaoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ManutencaoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ManutencaoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Manutencao(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.manutencaos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
