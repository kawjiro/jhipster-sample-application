/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ModeloEquipamentoDetailComponent } from 'app/entities/modelo-equipamento/modelo-equipamento-detail.component';
import { ModeloEquipamento } from 'app/shared/model/modelo-equipamento.model';

describe('Component Tests', () => {
    describe('ModeloEquipamento Management Detail Component', () => {
        let comp: ModeloEquipamentoDetailComponent;
        let fixture: ComponentFixture<ModeloEquipamentoDetailComponent>;
        const route = ({ data: of({ modeloEquipamento: new ModeloEquipamento(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ModeloEquipamentoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ModeloEquipamentoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ModeloEquipamentoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.modeloEquipamento).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
