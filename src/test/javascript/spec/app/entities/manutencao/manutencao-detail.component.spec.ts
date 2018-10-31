/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ManutencaoDetailComponent } from 'app/entities/manutencao/manutencao-detail.component';
import { Manutencao } from 'app/shared/model/manutencao.model';

describe('Component Tests', () => {
    describe('Manutencao Management Detail Component', () => {
        let comp: ManutencaoDetailComponent;
        let fixture: ComponentFixture<ManutencaoDetailComponent>;
        const route = ({ data: of({ manutencao: new Manutencao(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ManutencaoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ManutencaoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ManutencaoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.manutencao).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
