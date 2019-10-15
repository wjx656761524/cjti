import {Pipe, PipeTransform} from '@angular/core';

/*
* 分转元
*/
@Pipe({name: 'fzy'})
export class FZYPipePipe implements PipeTransform {
    transform(value: number): number {
        if (value == null || value === undefined) {
            return 0;
        }
        const v: number = value / 100;
        return parseFloat(v.toFixed(2));
    }
}
