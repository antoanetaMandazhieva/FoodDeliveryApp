export const setCookie = (cookieName, cookieValue, days) => {
    const expires = new Date(Date.now() + days * 86400000);
    document.cookie = `${cookieName}=${cookieValue}; expires=${expires}; path=/; secure; SameSite=Lax`
}

export const getCookie = (name) => {
    return decodeURIComponent(document.cookie)
        .split('; ')
        .find(row => row.startsWith(name))
        ?.split('=')[1]
}

export const deleteCookie = (name) => {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
}