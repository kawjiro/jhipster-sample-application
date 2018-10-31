/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { OrgaoPublicoComponent } from 'app/entities/orgao-publico/orgao-publico.component';
import { OrgaoPublicoService } from 'app/entities/orgao-publico/orgao-publico.service';
import { OrgaoPublico } from 'app/shared/model/orgao-publico.model';

describe('Component Tests', () => {
    describe('OrgaoPublico Management Component', () => {
        let comp: OrgaoPublicoComponent;
        let fixture: ComponentFixture<OrgaoPublicoComponent>;
        let service: OrgaoPublicoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [OrgaoPublicoComponent],
                providers: []
            })
                .overrideTemplate(OrgaoPublicoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrgaoPublicoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrgaoPublicoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new OrgaoPublico(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.orgaoPublicos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
