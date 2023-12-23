import { Observable, Observer, takeUntil } from 'rxjs';

declare module 'rxjs' {
    export interface Observable<T> {
        safeSubscribe(cancelled: Observable<any>,
            observerOrNext?: Partial<Observer<T>> | ((value: T) => void)):
            void;
    }
}

export function completeWith(lifecycle: Observable<any>) {
    return (observable: Observable<any>) => {
        const terminator$ = new Observable<void>(subscriber => {
            lifecycle.subscribe().add(() => {
                subscriber.next()
            })
        });

        return observable.pipe(takeUntil(terminator$))
    };
}

Observable.prototype.safeSubscribe = function<T>(lifecycle: Observable<any>,
    observerOrNext: Partial<Observer<T>> | ((value: T) => void) = {}):
    void {
    const observer = typeof observerOrNext === 'function' ?
        { next: observerOrNext } : observerOrNext;

    this.pipe(
        completeWith(lifecycle)
    ).subscribe({
        next: observer.next,
        complete: observer.complete,
        error: err => {
            this.safeSubscribe(lifecycle, observer);
            observer.error && observer.error(err);
        }
    });
}
