/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ServidorComponent } from 'app/entities/servidor/servidor.component';
import { ServidorService } from 'app/entities/servidor/servidor.service';
import { Servidor } from 'app/shared/model/servidor.model';

describe('Component Tests', () => {
    describe('Servidor Management Component', () => {
        let comp: ServidorComponent;
        let fixture: ComponentFixture<ServidorComponent>;
        let service: ServidorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ServidorComponent],
                providers: []
            })
                .overrideTemplate(ServidorComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ServidorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServidorService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Servidor(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.servidors[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
