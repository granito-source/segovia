import { Resource } from '@granito/ngx-hal-client';

export class Sentence extends Resource {
    id!: string;

    text!: string;
}
