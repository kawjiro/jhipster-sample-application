/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { UsoService } from 'app/entities/uso/uso.service';
import { IUso, Uso } from 'app/shared/model/uso.model';

describe('Service Tests', () => {
    describe('Uso Service', () => {
        let injector: TestBed;
        let service: UsoService;
        let httpMock: HttpTestingController;
        let elemDefault: IUso;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(UsoService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Uso(0, currentDate, currentDate, 'AAAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        dataInicio: currentDate.format(DATE_FORMAT),
                        dataFim: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Uso', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        dataInicio: currentDate.format(DATE_FORMAT),
                        dataFim: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dataInicio: currentDate,
                        dataFim: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Uso(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Uso', async () => {
                const returnedFromService = Object.assign(
                    {
                        dataInicio: currentDate.format(DATE_FORMAT),
                        dataFim: currentDate.format(DATE_FORMAT),
                        tipoEquipamento: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        dataInicio: currentDate,
                        dataFim: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Uso', async () => {
                const returnedFromService = Object.assign(
                    {
                        dataInicio: currentDate.format(DATE_FORMAT),
                        dataFim: currentDate.format(DATE_FORMAT),
                        tipoEquipamento: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dataInicio: currentDate,
                        dataFim: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Uso', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
