import { apiRequest } from "../services/api";

export function formDate(dateStr : string) : string {
    const date = new Date(dateStr);
    const year = date.getFullYear().toString();
    const month = (date.getMonth() + 1).toString();
    const day = date.getDate().toString();

    return (day.length === 1 ? "0" : "") + day + " " + (month.length === 1 ? "0" : "") + month + " " + year
}

export function dateFormatter(date: string) : string {
    return date.split(" ").reverse().join("-");
}

export function imagePathFormatter(imagePath : string) : string {
    if (imagePath.includes("http")) return imagePath;

    return window.location.origin + imagePath;
}
