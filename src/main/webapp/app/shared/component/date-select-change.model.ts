export class DateSelectChange {
    startTime: string;
    endTime: string;
    isRealTime = false;

    constructor(startTime: string, endTime: string) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
