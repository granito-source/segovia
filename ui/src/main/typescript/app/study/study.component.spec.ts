import { Title } from '@angular/platform-browser';
import { Spectator, byTestId, createComponentFactory,
    mockProvider } from '@ngneat/spectator/jest';
import { cold } from 'jest-marbles';
import { Sentence } from './sentence';
import { SentenceService } from './sentence.service';
import { StudyComponent } from './study.component';

describe('StudyComponent', () => {
    const createComponent = createComponentFactory({
        component: StudyComponent,
        shallow: true,
        providers: [
            mockProvider(Title)
        ]
    });

    function create(config: Partial<SentenceService> = {}):
        Spectator<StudyComponent> {
        return createComponent({
            providers: [
                mockProvider(SentenceService, {
                    sentences: cold(''),
                    ...config
                })
            ]
        });
    }

    it('gets created', () => {
        expect(create().component).toBeTruthy();
    });

    it('sets title', () => {
        expect(create().inject(Title).setTitle)
            .toHaveBeenCalledWith('Segovia: Study');
    });

    it('sets page header', () => {
        expect(create().query(byTestId('page-header'))).toHaveText('Study');
    });

    it('subscribes to sentence updates on init', () => {
        const sentences = create().inject(SentenceService).sentences;

        expect(sentences).toHaveSubscriptions('^');
    });

    it('unsubscribes from sentence updates on destroy', () => {
        const spectator = create();
        const sentences = spectator.inject(SentenceService).sentences;

        cold('-----t').subscribe(() => spectator.fixture.destroy());

        expect(sentences).toHaveSubscriptions('^----!');
    });

    it('presents no active sentence initially', () => {
        const spectator = create();

        expect(cold('')).toSatisfyOnFlush(() => {
            spectator.detectChanges();

            expect(spectator.query(byTestId('active-sentence'))).toBeFalsy();
        });
    });

    it('presents the sentense as active when one sentence', () => {
        const spectator = create({
            sentences: cold('--s', {
                s: [new Sentence({ text: 'One.' })]
            })
        });

        expect(cold('')).toSatisfyOnFlush(() => {
            spectator.detectChanges();

            expect(spectator.query(byTestId('active-sentence')))
                .toHaveText('One.');
        });
    });

    it('presents the last sentense as active when many sentences', () => {
        const spectator = create({
            sentences: cold('--s', {
                s: [
                    new Sentence({ text: 'One.' }),
                    new Sentence({ text: 'Two.' })
                ]
            })
        });

        expect(cold('')).toSatisfyOnFlush(() => {
            spectator.detectChanges();

            expect(spectator.query(byTestId('active-sentence')))
                .toHaveText('Two.');
        });
    });

    it('presents no active sentence when no sentences', () => {
        const spectator = create({
            sentences: cold('--s--e', {
                s: [new Sentence({ text: 'One.' })],
                e: []
            })
        });

        expect(cold('')).toSatisfyOnFlush(() => {
            spectator.detectChanges();

            expect(spectator.query(byTestId('active-sentence'))).toBeFalsy();
        });
    });
});
