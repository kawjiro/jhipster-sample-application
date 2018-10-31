import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IManutencao } from 'app/shared/model/manutencao.model';

type EntityResponseType = HttpResponse<IManutencao>;
type EntityArrayResponseType = HttpResponse<IManutencao[]>;

@Injectable({ providedIn: 'root' })
export class ManutencaoService {
    public resourceUrl = SERVER_API_URL + 'api/manutencaos';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/manutencaos';

    constructor(private http: HttpClient) {}

    create(manutencao: IManutencao): Observable<EntityResponseType> {
        return this.http.post<IManutencao>(this.resourceUrl, manutencao, { observe: 'response' });
    }

    update(manutencao: IManutencao): Observable<EntityResponseType> {
        return this.http.put<IManutencao>(this.resourceUrl, manutencao, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IManutencao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IManutencao[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IManutencao[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
