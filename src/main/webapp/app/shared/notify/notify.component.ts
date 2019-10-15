import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'cjtj-notify-content',
    template: `
        <div class="modal-header">
            <h6 class="modal-title">提示</h6>
            <button type="button" class="close" aria-label="Close" (click)="activeModal.dismiss('Cross click')">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
            <p class="p-1 fontS14">{{message}}</p>
        </div>
        <div class="modal-footer justify-content-center">
            <button type="button" class="btn btn-scondary" (click)="activeModal.close('Close click')">关闭</button>
        </div>
    `
})
export class CjtjNotifyComponent {
    @Input() message;

    constructor(public activeModal: NgbActiveModal) {
    }
}
