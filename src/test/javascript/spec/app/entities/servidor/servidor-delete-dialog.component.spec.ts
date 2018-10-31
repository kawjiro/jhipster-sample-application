/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ServidorDeleteDialogComponent } from 'app/entities/servidor/servidor-delete-dialog.component';
import { ServidorService } from 'app/entities/servidor/servidor.service';

describe('Component Tests', () => {
    describe('Servidor Management Delete Component', () => {
        let comp: ServidorDeleteDialogComponent;
        let fixture: ComponentFixture<ServidorDeleteDialogComponent>;
        let service: ServidorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ServidorDeleteDialogComponent]
            })
                .overrideTemplate(ServidorDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ServidorDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServidorService);
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
