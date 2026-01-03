import { Accessor, Collection, HalError } from '@granito/ngx-hal-client';
import { createHttpFactory, createSpyObject, mockProvider, SpectatorHttp,
    SpyObject } from '@ngneat/spectator/vitest';
import { cold } from '@granito/vitest-marbles';
import { Subject } from 'rxjs';
import { ApiRoot } from '../api/api-root';
import { ApiService } from '../api/api.service';
import { Sentence } from './sentence';
import { SentenceService } from './sentence.service';

describe('SentenceService', () => {
    const createService = createHttpFactory({
        service: SentenceService
    });

    let apiRoot: Subject<ApiRoot>;
    let spectator: SpectatorHttp<SentenceService>;
    let api: SpyObject<ApiRoot>;
    let accessor: SpyObject<Accessor>;

    beforeEach(() => {
        apiRoot = new Subject();
        api = createSpyObject(ApiRoot);
        accessor = createSpyObject(Accessor, { canRead: true });
        api.follow.andReturn(accessor);
        spectator = createService({
            providers: [
                mockProvider(ApiService, { root: apiRoot })
            ]
        });
    });

    it('gets created', () => {
        expect(spectator.service).toBeTruthy();
    });

    it('subscribes to API Root for the lifetime', () => {
        expect(apiRoot.observed).toBe(true);

        spectator.service.ngOnDestroy();

        expect(apiRoot.observed).toBe(false);
    });

    describe('#sentences', () => {
        it('presents nothing initially', () => {
            expect(spectator.service.sentences).toBeObservable(cold(''));
        });

        it('presents nothing when API root has no readable link', () => {
            accessor.castToWritable().canRead = false;

            cold('--t').subscribe(() => apiRoot.next(api));

            expect(spectator.service.sentences).toBeObservable(cold(''));
            expect(cold('')).toSatisfyOnFlush(() => {
                expect(api.follow).toHaveBeenCalledTimes(1);
                expect(api.follow)
                    .toHaveBeenCalledWith('sentences', undefined);
                expect(accessor.readCollection).not.toHaveBeenCalled();
            });
        });

        it('presents API results when API root has link', () => {
            accessor.readCollection.andReturn(cold('-s|', {
                s: new Collection(Sentence, {
                    _embedded: {
                        sentences: [
                            { id: 'one', text: 'One.' },
                            { id: 'two', text: 'Two.' }
                        ]
                    }
                })
            }));

            cold('--t').subscribe(() => apiRoot.next(api));

            expect(spectator.service.sentences).toBeObservable(cold('---s', {
                s: [
                    new Sentence({ id: 'one', text: 'One.' }),
                    new Sentence({ id: 'two', text: 'Two.' })
                ]
            }));
            expect(cold('')).toSatisfyOnFlush(() => {
                expect(api.follow).toHaveBeenCalledTimes(1);
                expect(api.follow)
                    .toHaveBeenCalledWith('sentences', undefined);
                expect(accessor.readCollection).toHaveBeenCalledTimes(1);
                expect(accessor.readCollection)
                    .toHaveBeenCalledWith(Sentence);
            });
        });

        it('presents nothing when API call fails', () => {
            accessor.readCollection.andReturn(cold('--#', undefined,
                new HalError({ message: 'read collection' })));

            cold('--t').subscribe(() => apiRoot.next(api));

            expect(spectator.service.sentences).toBeObservable(cold(''));
        });
    });
});
