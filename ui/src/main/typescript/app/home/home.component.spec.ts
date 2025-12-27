import { Title } from '@angular/platform-browser';
import { Spectator, byTestId, createComponentFactory,
    mockProvider } from '@ngneat/spectator/vitest';
import { HomeComponent } from './home.component';

describe('HomeComponent', () => {
    const createComponent = createComponentFactory({
        component: HomeComponent,
        shallow: true,
        providers: [
            mockProvider(Title)
        ]
    });
    let spectator: Spectator<HomeComponent>;

    beforeEach(() => spectator = createComponent());

    it('gets created', () => {
        expect(spectator.component).toBeTruthy();
    });

    it('sets title', () => {
        expect(spectator.inject(Title).setTitle)
            .toHaveBeenCalledWith('Segovia: Home');
    });

    it('sets page header', () => {
        expect(spectator.query(byTestId('page-header')))
            .toHaveText('Welcome to Segovia');
    });
});
