import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrgaoPublico } from 'app/shared/model/orgao-publico.model';

type EntityResponseType = HttpResponse<IOrgaoPublico>;
type EntityArrayResponseType = HttpResponse<IOrgaoPublico[]>;

@Injectable({ providedIn: 'root' })
export class OrgaoPublicoService {
    public resourceUrl = SERVER_API_URL + 'api/orgao-publicos';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/orgao-publicos';

    constructor(private http: HttpClient) {}

    create(orgaoPublico: IOrgaoPublico): Observable<EntityResponseType> {
        return this.http.post<IOrgaoPublico>(this.resourceUrl, orgaoPublico, { observe: 'response' });
    }

    update(orgaoPublico: IOrgaoPublico): Observable<EntityResponseType> {
        return this.http.put<IOrgaoPublico>(this.resourceUrl, orgaoPublico, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOrgaoPublico>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrgaoPublico[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrgaoPublico[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
