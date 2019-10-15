import {NgbDatepickerI18n, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {Injectable} from '@angular/core';

const I18N_VALUES = {
    'zh': {
        weekdays: ['一', '二', '三', '四', '五', '六', '日'],
        months: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
    }
    // other languages you would support
};

// Define custom service providing the months and weekdays translations
@Injectable()
export class CustomDatepickerI18n extends NgbDatepickerI18n {

    constructor() {
        super();
    }

    getWeekdayShortName(weekday: number): string {
        return I18N_VALUES['zh'].weekdays[weekday - 1];
    }

    getMonthShortName(month: number): string {
        return I18N_VALUES['zh'].months[month - 1];
    }

    getMonthFullName(month: number): string {
        return this.getMonthShortName(month);
    }

    getDayAriaLabel(date: NgbDateStruct): string {
        return "";
    }
}
