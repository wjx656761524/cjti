import {Pipe, PipeTransform} from '@angular/core';

/*
* 元转分
*/
@Pipe({name: 'yzf'})
export class YZFPipePipe implements PipeTransform {
    transform(value: number): number {
        if (value == null || value === undefined) {
            return 0;
        }
        const v: number = value * 100;
        return Math.trunc( v );
    }
}
