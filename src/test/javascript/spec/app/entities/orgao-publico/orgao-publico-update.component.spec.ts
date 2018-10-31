/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { OrgaoPublicoUpdateComponent } from 'app/entities/orgao-publico/orgao-publico-update.component';
import { OrgaoPublicoService } from 'app/entities/orgao-publico/orgao-publico.service';
import { OrgaoPublico } from 'app/shared/model/orgao-publico.model';

describe('Component Tests', () => {
    describe('OrgaoPublico Management Update Component', () => {
        let comp: OrgaoPublicoUpdateComponent;
        let fixture: ComponentFixture<OrgaoPublicoUpdateComponent>;
        let service: OrgaoPublicoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [OrgaoPublicoUpdateComponent]
            })
                .overrideTemplate(OrgaoPublicoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrgaoPublicoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrgaoPublicoService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new OrgaoPublico(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.orgaoPublico = entity;
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
                    const entity = new OrgaoPublico();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.orgaoPublico = entity;
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
