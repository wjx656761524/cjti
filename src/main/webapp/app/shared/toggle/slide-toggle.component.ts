import {Component, Input, Output, EventEmitter, ChangeDetectionStrategy, OnChanges, OnInit} from '@angular/core';

@Component({
    selector: 'cjtj-slide-toggle',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './slide-toggle.component.html',
})
export class SlideToggleComponent implements OnChanges{

    @Input() value = false;
    @Input() onText = 'ON';
    @Input() offText = 'OFF';
    @Input() onColor = '#3c7bfc';
    offColor = '#eceeef';

    @Output() valueChange = new EventEmitter<boolean>();

    onToggle() {
        this.value = !this.value;
        this.valueChange.emit(this.value);
    }

    get color(): string {
        return this.value ? this.onColor : this.offColor;
    }

    ngOnChanges() {


    }
}
