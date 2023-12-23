import { Component, OnDestroy, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Subject } from 'rxjs';
import '../rxjs/extension';
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

    private lifecycle$ = new Subject<void>();

    constructor(private title: Title, private sentenceService: SentenceService) {
    }

    ngOnInit(): void {
        this.title.setTitle('Segovia: Study');
        this.sentenceService.sentences.safeSubscribe(this.lifecycle$,
            sentences => this.post(sentences));
    }

    ngOnDestroy(): void {
        this.lifecycle$.complete();
    }

    private post(sentences: Sentence[]): void {
        const length = sentences.length;

        this.sentence = length > 0 ? sentences[length - 1].text : undefined;
    }
}
