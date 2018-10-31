/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ReservaComponent } from 'app/entities/reserva/reserva.component';
import { ReservaService } from 'app/entities/reserva/reserva.service';
import { Reserva } from 'app/shared/model/reserva.model';

describe('Component Tests', () => {
    describe('Reserva Management Component', () => {
        let comp: ReservaComponent;
        let fixture: ComponentFixture<ReservaComponent>;
        let service: ReservaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ReservaComponent],
                providers: []
            })
                .overrideTemplate(ReservaComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReservaComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReservaService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Reserva(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.reservas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
