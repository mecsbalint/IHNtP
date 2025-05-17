
export async function apiRequest({url, method = "GET", body = null, headers = {}, onUnauthorizedResponse = null}) {
  let user;

  try {
    user = JSON.parse(localStorage.getItem("ihntpUser"));
  } catch {
    user = {jwt: null};
  }

  const response = await fetch(url, {
    method,
    body,
    headers: {
      Authorization: !onUnauthorizedResponse ? "" : "Bearer " + user.jwt,
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
