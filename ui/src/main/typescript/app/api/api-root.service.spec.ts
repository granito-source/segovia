import { SpectatorService, createServiceFactory, createSpyObject, mockProvider } from '@ngneat/spectator/jest';
import { ApiRootService } from './api-root.service';
import { ApplicationInitStatus } from '@angular/core';
import { Accessor, HalClientService } from '@granito/ngx-hal-client';
import { fakeAsync, tick } from '@angular/core/testing';
import { ApiRoot } from './api-root';
import { cold } from 'jest-marbles';

describe('ApiRootService', () => {
    const createService = createServiceFactory({
        service: ApiRootService
    });
    let init: () => void;
    let donePromise: Promise<void>;
    let spectator: SpectatorService<ApiRootService>;

    beforeEach(() => {
        donePromise = new Promise(resolve => {
            init = resolve;
        });
        spectator = createService({
            providers: [
                mockProvider(ApplicationInitStatus, { donePromise }),
                mockProvider(HalClientService)
            ]
        });
    });

    it('gets created', () => {
        expect(spectator.service).toBeTruthy();
    });

    describe('#apiRoot', () => {
        it('presents nothing before app is ready', fakeAsync(() => {
            const hal = spectator.inject(HalClientService);

            tick();

            expect(hal.root).not.toHaveBeenCalled();
            expect(spectator.service.apiRoot).toBeObservable(cold(''));
        }));

        it('presents API root when app becomes ready', fakeAsync(() => {
            const accessor = createSpyObject(Accessor);
            const api = new ApiRoot({
                _links: {
                    self: { href: '/api/v1' }
                }
            });
            const hal = spectator.inject(HalClientService);

            hal.root.andReturn(accessor);
            accessor.read.andReturn(cold('--a|', { a: api }));

            init();
            tick();

            expect(hal.root).toHaveBeenCalledTimes(1);
            expect(hal.root).toHaveBeenCalledWith('/api/v1');
            expect(accessor.read).toHaveBeenCalledTimes(1);
            expect(accessor.read).toHaveBeenLastCalledWith(ApiRoot);
            expect(spectator.service.apiRoot).toBeObservable(cold('--a', {
                a: api
            }));
        }));
    });
});
