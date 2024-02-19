import { Component, OnDestroy, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { completeWith } from '@granito/ngx-hal-client';
import { Subject } from 'rxjs';
import { Sentence } from './sentence';
import { SentenceService } from './sentence.service';

@Component({
    selector: 'study',
    standalone: true,
    templateUrl: './study.component.html',
    styleUrl: './study.component.css'
})
export class StudyComponent implements OnInit, OnDestroy {
    sentence?: string;

    private lifetime$ = new Subject<void>();

    constructor(private title: Title, private sentenceService: SentenceService) {
    }

    ngOnInit(): void {
        this.title.setTitle('Segovia: Study');
        this.sentenceService.sentences.pipe(
            completeWith(this.lifetime$)
        ).subscribe(sentences => this.post(sentences));
    }

    ngOnDestroy(): void {
        this.lifetime$.complete();
    }

    private post(sentences: Sentence[]): void {
        const length = sentences.length;

        this.sentence = length > 0 ? sentences[length - 1].text : undefined;
    }
}
