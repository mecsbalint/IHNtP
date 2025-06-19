import { User } from "../types/User";

type ApiRequestParams = {
  url: string,
  method?: "GET" | "POST" | "DELETE" | "PUT" | "PATCH",
  body?: string | FormData,
  headers?: {[key: string]: string},
  onUnauthorizedResponse?: (() => void)
};

type ApiResponse<T = any> = {
  status: number,
  body: T | null
};

export async function apiRequest<T>({
  url, 
  method = "GET", 
  body,
  headers = {},
  onUnauthorizedResponse
} : ApiRequestParams) : Promise<ApiResponse<T>> {

  let jwt : string | null;
  const localStorageItem : string | null = localStorage.getItem("ihntpUser");
  
  jwt = localStorageItem === null || localStorageItem === "null" ? null : (JSON.parse(localStorageItem) as User).jwt;

  const response = await fetch(url, {
    method,
    headers: {
      Authorization: !onUnauthorizedResponse ? "" : "Bearer " + jwt,
      ...headers,
    },
    ...(method !== "GET" && body ? {body} : {})
  });

  if (response.status === 401) {
    onUnauthorizedResponse && onUnauthorizedResponse();
    return {status: response.status, body: null};
  }

  let responseBody = await response.json().catch(() => null);

  return {status: response.status, body: responseBody as T};
}
