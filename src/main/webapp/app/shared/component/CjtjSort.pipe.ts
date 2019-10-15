import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'cjtj-sort'})
export class CjtjSortPipe implements PipeTransform {
    transform(values: any[], predicate = '', reverse = false): any {
        if (predicate === '') {
            return reverse ? values.sort().reverse() : values.sort();
        }
        return values.sort((a, b) => {
            const aVal = this.extractVal(a, predicate);
            const bVal = this.extractVal(b, predicate);

            if (aVal < bVal) {
                return reverse ? 1 : -1;
            } else if (bVal < aVal) {
                return reverse ? -1 : 1;
            }
            return 0;
        });
    }

    extractVal(source: any, predicate = ''): any {
        let result = source;
        if (predicate.indexOf('.') !== -1) {
            const fieldArray = predicate.split('.');
            for (const field of fieldArray) {
                if (result && result[field]) {
                    result = result[field];
                } else {
                    result = null;
                    break;
                }
            }
        } else {
            result = source[predicate];
        }
        return result;
    }
}
