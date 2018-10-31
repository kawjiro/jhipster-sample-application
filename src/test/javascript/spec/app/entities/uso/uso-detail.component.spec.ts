/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { UsoDetailComponent } from 'app/entities/uso/uso-detail.component';
import { Uso } from 'app/shared/model/uso.model';

describe('Component Tests', () => {
    describe('Uso Management Detail Component', () => {
        let comp: UsoDetailComponent;
        let fixture: ComponentFixture<UsoDetailComponent>;
        const route = ({ data: of({ uso: new Uso(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [UsoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UsoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UsoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.uso).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
