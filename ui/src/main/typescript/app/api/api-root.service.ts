import { ApplicationInitStatus, Injectable } from '@angular/core';
import { HalClientService, defined, read } from '@granito/ngx-hal-client';
import { Observable, ReplaySubject, defer, map } from 'rxjs';
import { ApiRoot } from './api-root';

@Injectable({ providedIn: 'root' })
export class ApiRootService {
    private apiRoot$ = new ReplaySubject<ApiRoot>(1);

    get apiRoot(): Observable<ApiRoot> {
        return this.apiRoot$.asObservable();
    }

    constructor(init: ApplicationInitStatus, hal: HalClientService) {
        defer(() => init.donePromise).pipe(
            map(() => hal.root('/api/v1')),
            read(ApiRoot),
            defined()
        ).subscribe(api => this.apiRoot$.next(api));
    }
}
