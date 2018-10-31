/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { OrgaoPublicoDetailComponent } from 'app/entities/orgao-publico/orgao-publico-detail.component';
import { OrgaoPublico } from 'app/shared/model/orgao-publico.model';

describe('Component Tests', () => {
    describe('OrgaoPublico Management Detail Component', () => {
        let comp: OrgaoPublicoDetailComponent;
        let fixture: ComponentFixture<OrgaoPublicoDetailComponent>;
        const route = ({ data: of({ orgaoPublico: new OrgaoPublico(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [OrgaoPublicoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OrgaoPublicoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrgaoPublicoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.orgaoPublico).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
