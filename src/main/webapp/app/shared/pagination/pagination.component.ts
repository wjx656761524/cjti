import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PAGE_SIZE} from '../../app.constants';

@Component({
    selector: 'cjtj-pagination',
    templateUrl: './pagination.component.html',
})
export class PaginationComponent implements OnInit {

    count = 0;
    page = 1;
    start = -1;
    end = 0;
    @Input() pageSize = PAGE_SIZE;
    @Output() onPageChange = new EventEmitter<any>();

    ngOnInit(): void {
        this.onChangePage();
    }

    public restPage(page) {
        this.page = page;
        this.onChangePage();
    }

    @Input()
    set setPage(page) {
        this.page = page;
        this.onChangePage();
    }

    @Input()
    set setCount(count) {
        this.count = count;
        this.onChangePage();
    }

    onChangePage() {
        this.start = -1;
        this.end = 0;
        if (this.count && this.count > 0) {
            this.start = (this.page - 1) * this.pageSize;
            this.end = this.page * this.pageSize > this.count ? this.count : this.page * this.pageSize;
        }
    }

    showPage(pageNumber) {
        const pageData = {page: pageNumber, pageSize: this.pageSize};
        this.page = pageNumber;
        this.onChangePage();
        this.onPageChange.emit(pageData);
    }

}
