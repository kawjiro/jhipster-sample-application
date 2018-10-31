/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ServidorUpdateComponent } from 'app/entities/servidor/servidor-update.component';
import { ServidorService } from 'app/entities/servidor/servidor.service';
import { Servidor } from 'app/shared/model/servidor.model';

describe('Component Tests', () => {
    describe('Servidor Management Update Component', () => {
        let comp: ServidorUpdateComponent;
        let fixture: ComponentFixture<ServidorUpdateComponent>;
        let service: ServidorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ServidorUpdateComponent]
            })
                .overrideTemplate(ServidorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ServidorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServidorService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Servidor(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.servidor = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Servidor();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.servidor = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
