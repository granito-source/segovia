import { Inject, Injectable } from '@angular/core';
import { Accessor, HAL_ROOT } from '@granito/ngx-hal-client';
import { Observable, ReplaySubject } from 'rxjs';
import { ApiRoot } from './api-root';

@Injectable({ providedIn: 'root' })
export class ApiService {
    private root$ = new ReplaySubject<ApiRoot>(1);

    get root(): Observable<ApiRoot> {
        return this.root$.asObservable();
    }

    constructor(@Inject(HAL_ROOT) halRoot: Accessor) {
        halRoot.read(ApiRoot).subscribe(root => this.root$.next(root));
    }
}
