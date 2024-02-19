import { Injectable, OnDestroy } from '@angular/core';
import { Collection, completeWith, defined, follow, readCollection } from '@granito/ngx-hal-client';
import { Observable, ReplaySubject, catchError, map, of } from 'rxjs';
import { ApiRootService } from '../api/api-root.service';
import { Sentence } from './sentence';

@Injectable({ providedIn: 'root' })
export class SentenceService implements OnDestroy {
    private sentences$ = new ReplaySubject<Collection<Sentence>>(1);

    get sentences(): Observable<Sentence[]> {
        return this.sentences$.pipe(
            map(collection => collection.values)
        );
    }

    constructor(apiRootService: ApiRootService) {
        apiRootService.apiRoot.pipe(
            completeWith(this.sentences$),
            follow('sentences'),
            readCollection(Sentence),
            catchError(this.handleError),
            defined()
        ).subscribe(collection => this.sentences$.next(collection));
    }

    ngOnDestroy(): void {
        this.sentences$.complete();
    }

    private handleError(error: Error): Observable<undefined> {
        console.log(`error reading sentences: ${error.message}`);

        return of(undefined);
    }
}
