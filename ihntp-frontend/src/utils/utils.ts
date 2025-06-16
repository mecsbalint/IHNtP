
export function formDate(date : Date) : string {
    const year = date.getFullYear().toString();
    const month = (date.getMonth() + 1).toString();
    const day = date.getDate().toString();

    return day.length === 1 ? "0" : "" + day + " " + (month.length === 1 ? "0" : "") + month + " " + year
}
