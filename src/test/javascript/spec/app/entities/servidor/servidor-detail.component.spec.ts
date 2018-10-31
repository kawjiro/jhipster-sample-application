/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ServidorDetailComponent } from 'app/entities/servidor/servidor-detail.component';
import { Servidor } from 'app/shared/model/servidor.model';

describe('Component Tests', () => {
    describe('Servidor Management Detail Component', () => {
        let comp: ServidorDetailComponent;
        let fixture: ComponentFixture<ServidorDetailComponent>;
        const route = ({ data: of({ servidor: new Servidor(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ServidorDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ServidorDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ServidorDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.servidor).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
