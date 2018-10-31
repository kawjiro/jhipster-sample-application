/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { JustificativaDetailComponent } from 'app/entities/justificativa/justificativa-detail.component';
import { Justificativa } from 'app/shared/model/justificativa.model';

describe('Component Tests', () => {
    describe('Justificativa Management Detail Component', () => {
        let comp: JustificativaDetailComponent;
        let fixture: ComponentFixture<JustificativaDetailComponent>;
        const route = ({ data: of({ justificativa: new Justificativa(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [JustificativaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(JustificativaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(JustificativaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.justificativa).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
