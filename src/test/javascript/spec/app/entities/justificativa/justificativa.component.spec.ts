/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { JustificativaComponent } from 'app/entities/justificativa/justificativa.component';
import { JustificativaService } from 'app/entities/justificativa/justificativa.service';
import { Justificativa } from 'app/shared/model/justificativa.model';

describe('Component Tests', () => {
    describe('Justificativa Management Component', () => {
        let comp: JustificativaComponent;
        let fixture: ComponentFixture<JustificativaComponent>;
        let service: JustificativaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [JustificativaComponent],
                providers: []
            })
                .overrideTemplate(JustificativaComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(JustificativaComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JustificativaService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Justificativa(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.justificativas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
