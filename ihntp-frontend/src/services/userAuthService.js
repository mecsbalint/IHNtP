import { apiRequest } from "./api";

export async function loginUser(loginObj) {
    const responseObj = await apiRequest({url: "api/login", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(loginObj)});

    return responseObj;
}

export async function registrateUser(registrationObj) {
    const responseObj = await apiRequest({url: "api/registration", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(registrationObj)});

    return responseObj;
}
