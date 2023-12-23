import { Title } from '@angular/platform-browser';
import { Spectator, createComponentFactory,
    mockProvider } from '@ngneat/spectator/jest';
import { StudyComponent } from './study.component';

describe('StudyComponent', () => {
    const createComponent = createComponentFactory({
        component: StudyComponent,
        shallow: true,
        providers: [
            mockProvider(Title)
        ]
    });
    let spectator: Spectator<StudyComponent>;

    beforeEach(() => spectator = createComponent());

    it('gets created', () => {
        expect(spectator.component).toBeTruthy();
    });

    it('sets title', () => {
        expect(spectator.inject(Title).setTitle)
            .toHaveBeenCalledWith('Segovia: Study');
    });
});
