import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUso } from 'app/shared/model/uso.model';

type EntityResponseType = HttpResponse<IUso>;
type EntityArrayResponseType = HttpResponse<IUso[]>;

@Injectable({ providedIn: 'root' })
export class UsoService {
    public resourceUrl = SERVER_API_URL + 'api/usos';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/usos';

    constructor(private http: HttpClient) {}

    create(uso: IUso): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(uso);
        return this.http
            .post<IUso>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(uso: IUso): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(uso);
        return this.http
            .put<IUso>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IUso>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IUso[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IUso[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(uso: IUso): IUso {
        const copy: IUso = Object.assign({}, uso, {
            dataInicio: uso.dataInicio != null && uso.dataInicio.isValid() ? uso.dataInicio.format(DATE_FORMAT) : null,
            dataFim: uso.dataFim != null && uso.dataFim.isValid() ? uso.dataFim.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dataInicio = res.body.dataInicio != null ? moment(res.body.dataInicio) : null;
            res.body.dataFim = res.body.dataFim != null ? moment(res.body.dataFim) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((uso: IUso) => {
                uso.dataInicio = uso.dataInicio != null ? moment(uso.dataInicio) : null;
                uso.dataFim = uso.dataFim != null ? moment(uso.dataFim) : null;
            });
        }
        return res;
    }
}
