/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { EquipamentoDetailComponent } from 'app/entities/equipamento/equipamento-detail.component';
import { Equipamento } from 'app/shared/model/equipamento.model';

describe('Component Tests', () => {
    describe('Equipamento Management Detail Component', () => {
        let comp: EquipamentoDetailComponent;
        let fixture: ComponentFixture<EquipamentoDetailComponent>;
        const route = ({ data: of({ equipamento: new Equipamento(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [EquipamentoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(EquipamentoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EquipamentoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.equipamento).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
