import { Component, OnInit } from '@angular/core';
import { MatIconModule, MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { RouterLink, RouterOutlet } from '@angular/router';
import { CommonModule } from "@angular/common";

@Component({
    selector: 'app-root',
    imports: [
        CommonModule,
        RouterOutlet,
        RouterLink,
        MatIconModule
    ],
    templateUrl: './app.component.html',
    styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
    constructor(private title: Title, iconRegistry: MatIconRegistry,
        domSanitizer: DomSanitizer) {
        iconRegistry.addSvgIconSet(
            domSanitizer.bypassSecurityTrustResourceUrl('assets/mdi.svg'));
    }

    ngOnInit(): void {
        this.title.setTitle('Segovia');
    }
}
