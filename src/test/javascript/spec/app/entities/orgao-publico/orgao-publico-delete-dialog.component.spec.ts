/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { OrgaoPublicoDeleteDialogComponent } from 'app/entities/orgao-publico/orgao-publico-delete-dialog.component';
import { OrgaoPublicoService } from 'app/entities/orgao-publico/orgao-publico.service';

describe('Component Tests', () => {
    describe('OrgaoPublico Management Delete Component', () => {
        let comp: OrgaoPublicoDeleteDialogComponent;
        let fixture: ComponentFixture<OrgaoPublicoDeleteDialogComponent>;
        let service: OrgaoPublicoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [OrgaoPublicoDeleteDialogComponent]
            })
                .overrideTemplate(OrgaoPublicoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrgaoPublicoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrgaoPublicoService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
