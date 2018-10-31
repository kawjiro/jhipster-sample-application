/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { UsoComponent } from 'app/entities/uso/uso.component';
import { UsoService } from 'app/entities/uso/uso.service';
import { Uso } from 'app/shared/model/uso.model';

describe('Component Tests', () => {
    describe('Uso Management Component', () => {
        let comp: UsoComponent;
        let fixture: ComponentFixture<UsoComponent>;
        let service: UsoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [UsoComponent],
                providers: []
            })
                .overrideTemplate(UsoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UsoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UsoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Uso(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.usos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
