import {Injectable} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {CjtjNotifyComponent} from "app/shared/notify/notify.component";


@Injectable()
export class CjtjNotifyService {

    constructor(private modalService: NgbModal) {

    }

    /**
     * 打开消息提示窗
     * @param {string} message
     */
    openMessage(message: string) {
        const modalRef = this.modalService.open(CjtjNotifyComponent, {size: 'sm', centered: true});
        modalRef.componentInstance.message = message;
    }
}
