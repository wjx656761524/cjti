import {
    Directive,
    ElementRef,
    Input,
    OnChanges,
    Renderer,
    SimpleChanges
} from '@angular/core';

/**
 * A helper directive to focus buttons. You will only need this if using a custom template
 */
@Directive({
    selector: '[cjtjFocus]'
})
export class FocusDirective implements OnChanges {

    @Input() cjtjFocus: boolean;

    constructor(private elm: ElementRef) {}

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.mwlFocus && this.cjtjFocus === true) {
            this.elm.nativeElement.focus();
        }
    }

}
