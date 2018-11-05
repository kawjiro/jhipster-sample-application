import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReserva } from 'app/shared/model/reserva.model';

type EntityResponseType = HttpResponse<IReserva>;
type EntityArrayResponseType = HttpResponse<IReserva[]>;

@Injectable({ providedIn: 'root' })
export class ReservaService {
    public resourceUrl = SERVER_API_URL + 'api/reservas';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/reservas';

    constructor(private http: HttpClient) {}

    create(reserva: IReserva): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(reserva);
        return this.http
            .post<IReserva>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(reserva: IReserva): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(reserva);
        return this.http
            .put<IReserva>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IReserva>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReserva[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReserva[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(reserva: IReserva): IReserva {
        const copy: IReserva = Object.assign({}, reserva, {
            dataHoraReserva:
                reserva.dataHoraReserva != null && reserva.dataHoraReserva.isValid() ? reserva.dataHoraReserva.format(DATE_FORMAT) : null,
            dataHoraRetirdaPrev:
                reserva.dataHoraRetirdaPrev != null && reserva.dataHoraRetirdaPrev.isValid()
                    ? reserva.dataHoraRetirdaPrev.format(DATE_FORMAT)
                    : null,
            dataHoraDevolucaoPrev:
                reserva.dataHoraDevolucaoPrev != null && reserva.dataHoraDevolucaoPrev.isValid()
                    ? reserva.dataHoraDevolucaoPrev.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dataHoraReserva = res.body.dataHoraReserva != null ? moment(res.body.dataHoraReserva) : null;
            res.body.dataHoraRetirdaPrev = res.body.dataHoraRetirdaPrev != null ? moment(res.body.dataHoraRetirdaPrev) : null;
            res.body.dataHoraDevolucaoPrev = res.body.dataHoraDevolucaoPrev != null ? moment(res.body.dataHoraDevolucaoPrev) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((reserva: IReserva) => {
                reserva.dataHoraReserva = reserva.dataHoraReserva != null ? moment(reserva.dataHoraReserva) : null;
                reserva.dataHoraRetirdaPrev = reserva.dataHoraRetirdaPrev != null ? moment(reserva.dataHoraRetirdaPrev) : null;
                reserva.dataHoraDevolucaoPrev = reserva.dataHoraDevolucaoPrev != null ? moment(reserva.dataHoraDevolucaoPrev) : null;
            });
        }
        return res;
    }
}
