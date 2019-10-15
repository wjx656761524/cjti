import {
    Component,
    AfterViewInit,
} from '@angular/core';
import {ConfirmationPopoverWindowOptions} from './confirmation-popover-options.provider';

/**
 * @private
 */
@Component({
    styles: [`
        .popover {
            display: block;
        }

        .bs-popover-top .arrow, .bs-popover-bottom .arrow {
            left: 50%;
        }

        .bs-popover-left .arrow, .bs-popover-right .arrow {
            top: 50%;
        }

        .btn {
            transition: none;
        }
    `],
    template: `
        <ng-template #defaultTemplate let-options="options">
            <div [ngClass]="[
        'popover',
        options.placement,
        'popover-' + options.placement,
        'bs-popover-' + options.placement,
        options.popoverClass
      ]">
                <div class="popover-arrow arrow"></div>
                <h3 class="popover-title popover-header" [innerHTML]="options.title"></h3>
                <div class="popover-content popover-body">
                    <ng-container [ngTemplateOutlet]="options.contentTemplate || defaultContentTemplate" [ngTemplateOutletContext]="{options: options}"></ng-container>
                    <div class="row mb-2 ml-2 mr-2">
                        <div
                            class="mr-2"
                            [ngClass]="{'col-xs-offset-3 col-offset-3': options.hideCancelButton}"
                            *ngIf="!options.hideConfirmButton">
                            <button
                                type="button"
                                [cjtjFocus]="options.focusButton === 'confirm'"
                                [class]="'btn-mini btn-' + options.confirmButtonType"
                                (click)="options.onConfirm({clickEvent: $event})"
                                [innerHtml]="options.confirmText">
                            </button>
                        </div>
                        <div
                            class=""
                            [ngClass]="{'col-xs-offset-3 col-offset-3': options.hideConfirmButton}"
                            *ngIf="!options.hideCancelButton">
                            <button
                                type="button"
                                [cjtjFocus]="options.focusButton === 'cancel'"
                                [class]="'btn-mini btn-scondary'"
                                (click)="options.onCancel({clickEvent: $event})"
                                [innerHtml]="options.cancelText">
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </ng-template>
        <ng-template #defaultContentTemplate><p [innerHTML]="options.message"></p></ng-template>
        <ng-template
            [ngTemplateOutlet]="options.customTemplate || defaultTemplate"
            [ngTemplateOutletContext]="{options: options}">
        </ng-template>
    `
})
export class ConfirmationPopoverWindowComponent implements AfterViewInit {

    constructor(public options: ConfirmationPopoverWindowOptions) {
    }

    ngAfterViewInit(): void {
        this.options.onAfterViewInit();
    }

}
