
export async function apiRequest({url, method = "GET", body = null, headers = {}, onUnauthorizedResponse = null}) {
  const jwt = localStorage.getItem("ihntpJwt");

  const response = await fetch(url, {
    method,
    body,
    headers: {
      Authorization: jwt === "null" || !onUnauthorizedResponse ? "" : "Bearer " + jwt,
      ...headers,
    },
  });

  if (response.status === 401) {
    onUnauthorizedResponse && onUnauthorizedResponse();
    return {status: response.status, body: null};
  }

  const responseBody = await response.json().catch(() => null);

  return {status: response.status, body: responseBody};
}
