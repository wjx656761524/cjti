import {
    NgbCalendar,
    NgbCalendarGregorian, NgbDate,
    NgbDatepickerI18n,
    NgbDateStruct,
    NgbDropdown
} from "@ng-bootstrap/ng-bootstrap";
import {Component, EventEmitter, Input, Output, ViewChild} from "@angular/core";
import {CustomDatepickerI18n} from "app/shared/component/custom-datepicker-i18n.service";
import {DateSelectChange} from "app/shared";


const equals = (one: NgbDateStruct, two: NgbDateStruct) =>
    one && two && two.year === one.year && two.month === one.month && two.day === one.day;

const before = (one: NgbDateStruct, two: NgbDateStruct) =>
    !one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
        ? false : one.day < two.day : one.month < two.month : one.year < two.year;

const after = (one: NgbDateStruct, two: NgbDateStruct) =>
    !one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
        ? false : one.day > two.day : one.month > two.month : one.year > two.year;

@Component({
    selector: 'cjtj-report-date-select',
    templateUrl: './report-date-select.component.html',
    providers: [
        {provide: NgbDatepickerI18n, useClass: CustomDatepickerI18n},
        {provide: NgbCalendar, useClass: NgbCalendarGregorian}
    ],
})
export class ReportDateSelectComponent {

    @Output() onSelectChange = new EventEmitter<DateSelectChange>();

    @ViewChild('reportDateSelect') reportDateSelect: NgbDropdown;

    hoveredDate: NgbDateStruct;

    @Input() fromDate: NgbDate;
    @Input() toDate: NgbDate;
    lastDay: NgbDate;
    minDate: NgbDate;
    isRealTime = false;
    startDate: string;
    endDate: string;

    constructor(public calendar: NgbCalendar) {
        this.lastDay = calendar.getNext(calendar.getToday(), 'd', 0);
        this.fromDate = this.lastDay;
        this.toDate = this.lastDay;
        this.startDate = this.dateToString(this.fromDate);
        this.endDate = this.dateToString(this.toDate);
        this.minDate = calendar.getNext(calendar.getToday(), 'd', -30);
    }

    onDateChange(date: NgbDateStruct) {
        if (!this.fromDate && !this.toDate) {
            this.fromDate = this.convertDate(date);
            this.startDate = this.dateToString(this.fromDate);
        } else if (this.fromDate && !this.toDate && (after(date, this.fromDate) || equals(date, this.fromDate))) {
            this.toDate = this.convertDate(date);
            this.endDate = this.dateToString(this.toDate);
            console.log(`start:${this.fromDate},end:${this.toDate}`);
            this.isRealTime = false;
            if (this.onSelectChange) {
                this.onSelectChange.emit(new DateSelectChange(this.dateToString(this.fromDate), this.dateToString(this.toDate)));
            }
            this.reportDateSelect.close();
        } else {
            this.toDate = null;
            this.fromDate = this.convertDate(date);
            this.startDate = this.dateToString(this.fromDate);
        }
    }

    changeSelectDate(isRealTime: boolean, lastDays?: number) {
        if (isRealTime) {
            this.isRealTime = true;
            this.fromDate = null;
            this.toDate = null;
            const dateChange = new DateSelectChange(null, null);
            dateChange.isRealTime = true;
            this.onSelectChange.emit(dateChange);
        } else {
            this.isRealTime = false;
            this.fromDate = this.calendar.getNext(this.calendar.getToday(), 'd', -lastDays);
            this.toDate = this.calendar.getNext(this.calendar.getToday(), 'd', -1);
            this.startDate = this.dateToString(this.fromDate);
            this.endDate = this.dateToString(this.toDate);
            this.onSelectChange.emit(new DateSelectChange(this.dateToString(this.fromDate), this.dateToString(this.toDate)));
        }

        this.reportDateSelect.close();
    }

    convertDate(date: NgbDateStruct): NgbDate {
        if (date == null) {
            return null;
        }
        return NgbDate.from({year: date.year, month: date.month, day: date.day});
    }

    dateToString(date: NgbDate): string {
        const year = date.year;
        const month = date.month;
        const day = date.day;
        const str = `${year}-${month < 10 ? ('0' + month) : month}-${day < 10 ? ('0' + day) : day}`;
        return str;
    }

    isOutRange = (date) => before(date, this.minDate) || after(date, this.lastDay);
    isHovered = (date) => this.fromDate && !this.toDate && this.hoveredDate && after(date, this.fromDate) && before(date, this.hoveredDate);
    isInside = (date) => after(date, this.fromDate) && before(date, this.toDate);
    isFrom = (date) => equals(date, this.fromDate);
    isTo = (date) => equals(date, this.toDate);
}
