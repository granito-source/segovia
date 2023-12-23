import { SpectatorService, createServiceFactory } from '@ngneat/spectator/jest';
import { SentenceService } from './sentence.service';

describe('SentenceService', () => {
    const createService = createServiceFactory({
        service: SentenceService
    });
    let spectator: SpectatorService<SentenceService>;

    beforeEach(() => spectator = createService());

    it('gets created', () => {
        expect(spectator.service).toBeTruthy();
    });
});
