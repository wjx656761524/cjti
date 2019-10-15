import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {NgbDropdown} from '@ng-bootstrap/ng-bootstrap';
import {Principal} from '../auth/principal.service';
import {SERVER_API_URL} from '../../app.constants';
import {HttpClient, HttpParams} from '@angular/common/http';
import {DEFAULT_COLUMNS, REPORT_FIELDS} from '../../report.constants';

@Component({
    selector: 'cjtj-report-columns-select',
    templateUrl: './report-columns-select.component.html',
})
export class ReportColumnsSelectComponent implements OnInit {

    private resourceUrl = SERVER_API_URL + 'api/columns';
    allColumns = REPORT_FIELDS;
    selectColumns= [...DEFAULT_COLUMNS];
    selectColumnsSnapshot = [...DEFAULT_COLUMNS];
    @Output() onSelectChange = new EventEmitter<any[]>();
    @Input() business: string;
    @ViewChild('columnsSelect') columnsSelect: NgbDropdown;

    constructor(public principal: Principal,
                private http: HttpClient) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.loadSelectColumns();
        });
    }

    updateSelectColumn(column, event) {
        if (event.target.checked) {
            this.selectColumns.push(column.field);
        } else {
            this.removeSelectColumn(column.field);
        }
    }

    removeSelectColumn(columnField) {
        const index: number = this.selectColumns.indexOf(columnField);
        if (index !== -1) {
            this.selectColumns.splice(index, 1);
        }
    }

    enterSelect() {
        this.setSelect(this.selectColumns);
        this.postUpdateSelect(this.selectColumns);
        this.columnsSelect.close();
    }

    setSelect(columns) {
        this.onSelectChange.emit([...columns]);
        this.selectColumnsSnapshot = [...columns];
    }

    cancelSelect() {
        this.selectColumns = [...this.selectColumnsSnapshot];
        this.columnsSelect.close();
    }

    private loadSelectColumns() {
        const params = new HttpParams()
            .set('business', this.business);
        this.http
            .get(`${this.resourceUrl}/getSelectColumns`, {params}).subscribe(
            (response) => {
                if (response != null) {
                    let res: any;
                    res = response;
                    this.selectColumns = res;
                    this.setSelect(res);
                }
            }
        );
    }

    private postUpdateSelect(selectColumns: any[]) {
        const params = new HttpParams()
            .set('business', this.business)
            .set('columns', selectColumns.join());
        this.http
            .post(`${this.resourceUrl}/updateSelectColumns`, null, {params}).subscribe(
            (response) => {
                // console.log(response);
            }
        );
    }
}
